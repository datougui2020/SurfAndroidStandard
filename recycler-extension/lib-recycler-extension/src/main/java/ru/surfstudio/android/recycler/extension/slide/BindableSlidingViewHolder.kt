package ru.surfstudio.android.recycler.extension.slide

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.layout_bindable_sliding_view_holder.view.*
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.slide.BindableSlidingViewHolder.InteractionState.*
import ru.surfstudio.android.recycler.extension.util.dpToPx
import ru.surfstudio.easyadapter.carousel.R
import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * [BindableViewHolder] with possibility of horizontal sliding to reveal side-buttons.
 *
 * To properly setup [RecyclerView] for sliding functionality - use helper [SlidingHelper].
 * */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BindableSlidingViewHolder<T : Any>(parent: ViewGroup, @LayoutRes layoutRes: Int) :
        BindableViewHolder<T>(parent, R.layout.layout_bindable_sliding_view_holder) {

    private companion object {
        const val SLIDE_ANIM_DURATION = 300L
        const val SWIPE_VELOCITY_THRESHOLD_DP = 600f
        const val TOUCH_INTERCEPT_THRESHOLD_DP = 40f
    }

    /**
     * Internal interaction state.
     *
     * @property MAIN_CONTENT User interact's with main content of `ViewHolder`.
     * @property LEFT_BUTTONS User interact's with left buttons (`ViewHolder` translated to the right).
     * @property RIGHT_BUTTONS User interact's with right buttons (`ViewHolder` translated to the left).
     * */
    protected enum class InteractionState {
        MAIN_CONTENT,
        LEFT_BUTTONS,
        RIGHT_BUTTONS
    }

    // Views
    private val leftButtonsContainer = itemView.sliding_left_buttons_container
    private val rightButtonsContainer = itemView.sliding_right_buttons_container
    private val contentContainer = itemView.sliding_content_container
    private val contentBlockerView = itemView.sliding_content_blocker_view

    // View groups
    private val slidingViews = listOf(contentContainer, leftButtonsContainer, rightButtonsContainer, contentBlockerView)
    private val buttonViews by lazy { buttonContainers.flatMap { it.children.toList() } }
    private val buttonContainers = listOf(leftButtonsContainer, rightButtonsContainer)

    // Flags
    private var isInitialized = false
    private var isAnimatingSlide = false

    // Shortcuts
    private val currentSlideTranslation get() = contentContainer.translationX
    private val isReachedLeftContainer get() = currentSlideTranslation >= leftContainerTriggerPosition
    private val isReachedRightContainer get() = currentSlideTranslation <= rightContainerTriggerPosition
    private val hasLeftButtons get() = leftButtons.isNotEmpty()
    private val hasRightButtons get() = rightButtons.isNotEmpty()

    /** Width of screen in pixels. Used to calculate slide animation duration. */
    private val screenWidthPx = context.resources.displayMetrics.widthPixels

    /**
     *  Width of left buttons container. Also defines how far buttons should scroll to the right.
     *
     * **If we're using just `width`, not `width - 1` - we're getting little of empty space
     * between start of the screen and container. Code below resolves this issue.**
     * */
    private val leftContainerWidth by lazy { leftButtonsContainer.width.toFloat() - context.dpToPx(1) }

    /** Width of right buttons container. Also defines how far buttons should scroll to the left. */
    private val rightContainerWidth by lazy { rightButtonsContainer.width.toFloat() }

    /** Amount of translation used to decide is current state is [InteractionState.LEFT_BUTTONS] or not. */
    private val leftContainerTriggerPosition by lazy { leftContainerWidth / 2 }

    /** Amount of translation used to decide is current state is [InteractionState.RIGHT_BUTTONS] or not. */
    private val rightContainerTriggerPosition by lazy { -rightContainerWidth / 2 }

    /** Internal interaction state. */
    protected var interactionState = MAIN_CONTENT
        private set(value) {
            field = value
            contentBlockerView.isInvisible = value == MAIN_CONTENT
        }

    /** Amount of horizontal velocity, needed to change [InteractionState]. */
    protected open val swipeVelocityThreshold = context.dpToPx(SWIPE_VELOCITY_THRESHOLD_DP).toFloat()

    /** Amount of horizontal scrolled distance, needed to intercept the user's touch. */
    protected open val touchInterceptThreshold = context.dpToPx(TOUCH_INTERCEPT_THRESHOLD_DP).toFloat()

    /** List of buttons to inflate into the left container. */
    protected open val leftButtons: List<View> = emptyList()

    /** List of buttons to inflate into the right container. */
    protected open val rightButtons: List<View> = emptyList()

    init {
        View.inflate(context, layoutRes, contentContainer)
        contentBlockerView.setOnClickListener { onContentBlockerClicked() }
    }

    /** Callback, used to bind [data] to the particular [buttonView]. */
    protected open fun onBindButton(data: T, buttonView: View) {
        /* empty body */
    }

    /** Callback, used to response to user's [buttonView] click. */
    protected open fun onButtonClicked(buttonView: View) {
        /* empty body */
    }

    /**
     * Callback, used to response to user's click on [contentBlockerView].
     *
     * Default implementation call's `hideButtons()` method.
     * */
    protected open fun onContentBlockerClicked() {
        hideButtons()
    }

    /**
     * Callback, used to bind [data] to this `ViewHolder`.
     *
     * **Call `super.bind()` to bind data to buttons.**
     * */
    @CallSuper
    override fun bind(data: T) {
        bindButtons(data)
    }


    /** Callback, used to decide: are we intercepting this touch event of [RecyclerView]? */
    @CallSuper
    open fun onInterceptTouchEvent(touchTranslationX: Float): Boolean {
        return when (interactionState) {
            MAIN_CONTENT -> when {
                hasLeftButtons && touchTranslationX >= touchInterceptThreshold -> true
                hasRightButtons && touchTranslationX <= -touchInterceptThreshold -> true
                else -> false
            }
            RIGHT_BUTTONS -> when {
                touchTranslationX >= touchInterceptThreshold -> true
                else -> false
            }
            LEFT_BUTTONS -> when {
                touchTranslationX <= -touchInterceptThreshold -> true
                else -> false
            }
        }
    }

    /** Callback, used to response on just started user sliding gesture. */
    open fun onSlidingStarted() {
        /* empty body */
    }

    /** Callback, used to handle user sliding gesture. */
    @CallSuper
    open fun onSliding(touchTranslationX: Float) {
        relativeMoveContainersTo(touchTranslationX, shouldAnimate = false)
    }

    /** Callback, used to decide what to do when user ended his sliding gesture. */
    @CallSuper
    open fun onSlidingEnded(touchVelocityX: Float) {
        when (interactionState) {
            MAIN_CONTENT -> when {
                touchVelocityX >= swipeVelocityThreshold || isReachedLeftContainer -> showLeftButtons()
                touchVelocityX <= -swipeVelocityThreshold || isReachedRightContainer -> showRightButtons()
                else -> hideButtons()
            }
            RIGHT_BUTTONS -> when {
                touchVelocityX >= swipeVelocityThreshold && isReachedLeftContainer -> showLeftButtons()
                touchVelocityX >= swipeVelocityThreshold -> hideButtons()
                isReachedRightContainer -> showRightButtons()
                else -> hideButtons()
            }
            LEFT_BUTTONS -> when {
                touchVelocityX <= -swipeVelocityThreshold && isReachedRightContainer -> showRightButtons()
                touchVelocityX <= -swipeVelocityThreshold -> hideButtons()
                isReachedLeftContainer -> showLeftButtons()
                else -> hideButtons()
            }
        }
    }

    fun showLeftButtons(shouldAnimate: Boolean = true) {
        interactionState = LEFT_BUTTONS
        moveContainersTo(leftContainerWidth, shouldAnimate)
    }

    fun showRightButtons(shouldAnimate: Boolean = true) {
        interactionState = RIGHT_BUTTONS
        moveContainersTo(-rightContainerWidth, shouldAnimate)
    }

    fun hideButtons(shouldAnimate: Boolean = true) {
        interactionState = MAIN_CONTENT
        moveContainersTo(0f, shouldAnimate)
    }

    protected fun relativeMoveContainersTo(relativeTranslation: Float, shouldAnimate: Boolean) {
        val translationOrigin = when (interactionState) {
            MAIN_CONTENT -> 0f
            LEFT_BUTTONS -> leftContainerWidth
            RIGHT_BUTTONS -> -rightContainerWidth
        }
        val shiftedTranslation = translationOrigin + relativeTranslation
        val normalizedTranslation = when {
            shiftedTranslation <= -rightContainerWidth -> -rightContainerWidth
            shiftedTranslation >= leftContainerWidth -> leftContainerWidth
            else -> shiftedTranslation
        }
        moveContainersTo(normalizedTranslation, shouldAnimate)
    }

    protected fun moveContainersTo(absoluteTranslation: Float, shouldAnimate: Boolean) {
        if (isAnimatingSlide) {
            slidingViews.forEach(View::clearAnimation)
            isAnimatingSlide = false
        }
        when {
            !shouldAnimate -> slidingViews.forEach { it.translationX = absoluteTranslation }
            else -> {
                val animDuration = calculateSlideAnimDuration(
                        screenWidth = screenWidthPx,
                        from = currentSlideTranslation,
                        to = absoluteTranslation,
                        initialDuration = SLIDE_ANIM_DURATION
                )
                isAnimatingSlide = true
                slidingViews.forEach { slidingView ->
                    slidingView.animateSlideTo(
                            targetTranslation = absoluteTranslation,
                            duration = animDuration,
                            interpolator = DecelerateInterpolator(),
                            endAction = { isAnimatingSlide = false }
                    )
                }
            }
        }
    }

    private fun bindButtons(data: T) {
        if (!isInitialized) {
            leftButtons.forEach(leftButtonsContainer::addView)
            rightButtons.forEach(rightButtonsContainer::addView)
            buttonViews.forEach { button -> button.setOnClickListener { onButtonClicked(button) } }
            isInitialized = true
        }
        buttonViews.forEach { button -> onBindButton(data, button) }
    }

    private fun calculateSlideAnimDuration(
            screenWidth: Int,
            from: Float,
            to: Float,
            initialDuration: Long
    ): Long {
        val distanceToTravel = abs(from - to)
        return (distanceToTravel / screenWidth * initialDuration).roundToLong()
    }

    private fun View.animateSlideTo(
            targetTranslation: Float,
            duration: Long,
            interpolator: Interpolator,
            endAction: () -> Unit = { }
    ) {
        clearAnimation()
        animate().translationX(targetTranslation)
                .setDuration(duration)
                .setInterpolator(interpolator)
                .withEndAction(endAction)
                .start()
    }

}