/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.custom.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat.getDrawable

private const val DEFAULT_MAX_LINES: Int = 1

private const val START_DRAWABLE_POSITION: Int = 0
private const val TOP_DRAWABLE_POSITION: Int = 1
private const val END_DRAWABLE_POSITION: Int = 2
private const val BOTTOM_DRAWABLE_POSITION: Int = 3

/**
 * Виджет для отображения текста с подписью
 *
 * Изменять параметры форматирования текста можно посредством атрибутов в xml.
 * Возможно изменение заголовка и подписи по отдельности.
 */
class TitleSubtitleView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var titleView: TextView = TextView(context)
    private var subTitleView: TextView = TextView(context)

    private var defaultTitle: String = "Title"
        set(value) {
            field = value
            titleView.text = field
        }

    private var defaultSubTitle: String = "SubTitle"
        set(value) {
            field = value
            subTitleView.text = field
        }

    var titleText: String = defaultTitle
        set(value) {
            field = value
            updateView()
        }

    var subTitleText: String = defaultSubTitle
        set(value) {
            field = value
            updateView()
        }

    var titleVisibility: Int = View.VISIBLE
        get() {
            return titleView.visibility
        }
        set(value) {
            field = value
            titleView.visibility = value
        }

    var subTitleVisibility: Int = View.VISIBLE
        get() {
            return subTitleView.visibility
        }
        set(value) {
            field = value
            subTitleView.visibility = value
        }

    @StyleRes
    var titleTextAppearance: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                TextViewCompat.setTextAppearance(titleView, value)
            }
        }

    @StyleRes
    var subTitleTextAppearance: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                TextViewCompat.setTextAppearance(subTitleView, value)
            }
        }

    var titleLines: Int = -1
        set(value) {
            field = value
            titleView.setLines(value)
        }

    var subTitleLines: Int = -1
        set(value) {
            field = value
            subTitleView.setLines(value)
        }

    var titleMaxLines: Int = -1
        set(value) {
            field = value
            titleView.maxLines = value
        }

    var subTitleMaxLines: Int = -1
        set(value) {
            field = value
            subTitleView.maxLines = value
        }

    var titleStartDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(titleView, value, START_DRAWABLE_POSITION)
        }

    var subTitleStartDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(subTitleView, value, START_DRAWABLE_POSITION)
        }

    var titleTopDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(titleView, value, TOP_DRAWABLE_POSITION)
        }

    var subTitleTopDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(subTitleView, value, TOP_DRAWABLE_POSITION)
        }

    var titleEndDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(titleView, value, END_DRAWABLE_POSITION)
        }

    var subTitleEndDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(subTitleView, value, END_DRAWABLE_POSITION)
        }

    var titleBottomDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(titleView, value, BOTTOM_DRAWABLE_POSITION)
        }

    var subTitleBottomDrawableId: Int = -1
        set(value) {
            field = value
            setupDrawable(subTitleView, value, BOTTOM_DRAWABLE_POSITION)
        }

    var onTitleClickListenerCallback: ((String) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                titleView.setOnClickListener { value(titleText) }
            } else {
                titleView.setOnClickListener(null)
                titleView.isClickable = false
            }
        }

    var titleStartDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(titleView, value, START_DRAWABLE_POSITION)
        }

    var subTitleStartDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(subTitleView, value, START_DRAWABLE_POSITION)
        }

    var titleTopDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(titleView, value, TOP_DRAWABLE_POSITION)
        }

    var subTitleTopDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(subTitleView, value, TOP_DRAWABLE_POSITION)
        }

    var titleEndDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(titleView, value, END_DRAWABLE_POSITION)
        }

    var subTitleEndDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(subTitleView, value, END_DRAWABLE_POSITION)
        }

    var titleBottomDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(titleView, value, BOTTOM_DRAWABLE_POSITION)
        }

    var subTitleBottomDrawable: Drawable? = null
        set(value) {
            field = value
            setupDrawable(subTitleView, value, BOTTOM_DRAWABLE_POSITION)
        }

    var onSubTitleClickListenerCallback: ((String) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                subTitleView.setOnClickListener { value(subTitleText) }
            } else {
                subTitleView.setOnClickListener(null)
                subTitleView.isClickable = false
            }
        }

    init {
        orientation = LinearLayout.VERTICAL

        addViews()
        applyAttrs(attributeSet)
    }

    /**
     * Возвращает заголовок к дефолтному значению
     */
    fun resetTitleText() {
        titleText = defaultTitle
    }

    /**
     * Возвращает подзаголовок к дефолтному значению
     */
    fun resetSubTitleText() {
        subTitleText = defaultSubTitle
    }

    private fun addViews() {
        addView(titleView)
        addView(subTitleView)
    }

    private fun updateView() {
        titleView.text = titleText
        subTitleView.text = subTitleText
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val ta: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TitleSubtitleView, 0, 0)

        //заголовок
        setupTitle(ta)
        //подзаголовок
        setupSubTitle(ta)

        ta.recycle()
    }

    private fun setupTitle(ta: TypedArray) {
        with(titleView) {
            defaultTitle = ta.getString(R.styleable.TitleSubtitleView_titleText) ?: defaultTitle
            titleText = defaultTitle

            titleTextAppearance = ta.getResourceId(R.styleable.TitleSubtitleView_titleTextAppearance, -1)
            setupTextSize(ta, R.styleable.TitleSubtitleView_titleTextSize)
            setupTextColor(ta, R.styleable.TitleSubtitleView_titleTextColor)

            setLineSpacing(ta.getDimension(R.styleable.TitleSubtitleView_titleLineSpacingExtra, 0f), 1f)

            setPadding(
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingStart, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingTop, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingEnd, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingBottom, 0)
            )

            titleLines = ta.getInt(R.styleable.TitleSubtitleView_titleLines, lineCount)
            titleMaxLines = ta.getInt(R.styleable.TitleSubtitleView_titleMaxLines, DEFAULT_MAX_LINES)

            gravity = ta.getInt(R.styleable.TitleSubtitleView_titleGravity, gravity)

            setBackgroundColor(ta.getColor(
                    R.styleable.TitleSubtitleView_titleBackgroundColor,
                    ContextCompat.getColor(context, android.R.color.transparent)
            ))

            ellipsize = getEllipsizeFromResource(ta, R.styleable.TitleSubtitleView_titleEllipsize)
            visibility = getVisibilityFromResource(ta, R.styleable.TitleSubtitleView_titleVisibility)

            compoundDrawablePadding = ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titleDrawablePadding, 0)
            setCompoundDrawablesWithIntrinsicBounds(
                    ta.getResourceId(R.styleable.TitleSubtitleView_titleDrawableStart, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_titleDrawableTop, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_titleDrawableEnd, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_titleDrawableBottom, 0)
            )
        }
    }

    private fun setupSubTitle(ta: TypedArray) {
        with(subTitleView) {
            defaultSubTitle = ta.getString(R.styleable.TitleSubtitleView_subTitleText)
                    ?: defaultSubTitle
            subTitleText = defaultSubTitle

            subTitleTextAppearance = ta.getResourceId(R.styleable.TitleSubtitleView_subTitleTextAppearance, -1)
            setupTextSize(ta, R.styleable.TitleSubtitleView_subTitleTextSize)
            setupTextColor(ta, R.styleable.TitleSubtitleView_subTitleTextColor)

            setLineSpacing(ta.getDimension(R.styleable.TitleSubtitleView_subTitleLineSpacingExtra, 0f), 1f)

            setPadding(
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingStart, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingTop, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingEnd, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingBottom, 0)
            )

            subTitleLines = ta.getInt(R.styleable.TitleSubtitleView_subTitleLines, lineCount)
            subTitleMaxLines = ta.getInt(R.styleable.TitleSubtitleView_subTitleMaxLines, DEFAULT_MAX_LINES)

            gravity = ta.getInt(R.styleable.TitleSubtitleView_subTitleGravity, gravity)

            setBackgroundColor(ta.getColor(
                    R.styleable.TitleSubtitleView_subTitleBackgroundColor,
                    ContextCompat.getColor(context, android.R.color.transparent)
            ))

            ellipsize = getEllipsizeFromResource(ta, R.styleable.TitleSubtitleView_subTitleEllipsize)
            visibility = getVisibilityFromResource(ta, R.styleable.TitleSubtitleView_subTitleVisibility)

            compoundDrawablePadding = ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitleDrawablePadding, 0)
            setCompoundDrawablesWithIntrinsicBounds(
                    ta.getResourceId(R.styleable.TitleSubtitleView_subTitleDrawableStart, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_subTitleDrawableTop, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_subTitleDrawableEnd, 0),
                    ta.getResourceId(R.styleable.TitleSubtitleView_subTitleDrawableBottom, 0)
            )
        }
    }

    private fun TextView.setupTextSize(ta: TypedArray, index: Int) {
        val size = ta.getDimensionPixelSize(index, -1).toFloat()
        if (size > 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        }
    }

    private fun TextView.setupTextColor(ta: TypedArray, index: Int) {
        val textColor = ta.getColor(index, -1)
        if (textColor != -1) {
            setTextColor(textColor)
        }
    }

    private fun getEllipsizeFromResource(ta: TypedArray, index: Int): TextUtils.TruncateAt? =
            when (ta.getInt(index, 0)) {
                1 -> TextUtils.TruncateAt.START
                2 -> TextUtils.TruncateAt.MIDDLE
                3 -> TextUtils.TruncateAt.END
                4 -> TextUtils.TruncateAt.MARQUEE
                else -> TextUtils.TruncateAt.END
            }

    private fun getVisibilityFromResource(ta: TypedArray, index: Int): Int =
            when (ta.getInt(index, 0)) {
                0 -> View.VISIBLE
                1 -> View.INVISIBLE
                2 -> View.GONE
                else -> View.VISIBLE
            }

    private fun setupDrawable(
            textView: TextView,
            drawableId: Int,
            drawablePosition: Int
    ) {
        val textViewDrawables = textView.compoundDrawables
        textViewDrawables[drawablePosition] = getDrawable(context, drawableId)
        textView.setCompoundDrawablesWithIntrinsicBounds(
                textViewDrawables[START_DRAWABLE_POSITION],
                textViewDrawables[TOP_DRAWABLE_POSITION],
                textViewDrawables[END_DRAWABLE_POSITION],
                textViewDrawables[BOTTOM_DRAWABLE_POSITION]
        )
    }

    private fun setupDrawable(
            textView: TextView,
            drawable: Drawable?,
            drawablePosition: Int
    ) {
        if (drawable != null) {
            val textViewDrawables = textView.compoundDrawables
            textViewDrawables[drawablePosition] = drawable
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    textViewDrawables[START_DRAWABLE_POSITION],
                    textViewDrawables[TOP_DRAWABLE_POSITION],
                    textViewDrawables[END_DRAWABLE_POSITION],
                    textViewDrawables[BOTTOM_DRAWABLE_POSITION]
            )
        }
    }

    private fun setDrawables(
            textView: TextView,
            startDrawableId: Int = 0,
            topDrawableId: Int = 0,
            endDrawableId: Int = 0,
            bottomDrawableId: Int = 0
    ) {
        val textViewDrawables = textView.compoundDrawables
        textView.setCompoundDrawablesWithIntrinsicBounds(
                if (startDrawableId != 0) getDrawable(context, startDrawableId) else textViewDrawables[START_DRAWABLE_POSITION],
                if (topDrawableId != 0) getDrawable(context, topDrawableId) else textViewDrawables[TOP_DRAWABLE_POSITION],
                if (endDrawableId != 0) getDrawable(context, endDrawableId) else textViewDrawables[END_DRAWABLE_POSITION],
                if (bottomDrawableId != 0) getDrawable(context, bottomDrawableId) else textViewDrawables[BOTTOM_DRAWABLE_POSITION]
        )
    }

    private fun setDrawables(
            textView: TextView,
            startDrawable: Drawable? = null,
            topDrawable: Drawable? = null,
            endDrawable: Drawable? = null,
            bottomDrawable: Drawable? = null
    ) {
        val textViewDrawables = textView.compoundDrawables
        textView.setCompoundDrawablesWithIntrinsicBounds(
                startDrawable ?: textViewDrawables[START_DRAWABLE_POSITION],
                topDrawable ?: textViewDrawables[TOP_DRAWABLE_POSITION],
                endDrawable ?: textViewDrawables[END_DRAWABLE_POSITION],
                bottomDrawable ?: textViewDrawables[BOTTOM_DRAWABLE_POSITION]
        )
    }

    fun setTitleDrawables(
            startDrawableId: Int = 0,
            topDrawableId: Int = 0,
            endDrawableId: Int = 0,
            bottomDrawableId: Int = 0
    ) {
        setDrawables(titleView, startDrawableId, topDrawableId, endDrawableId, bottomDrawableId)
    }

    fun setTitleDrawables(
            startDrawable: Drawable? = null,
            topDrawable: Drawable? = null,
            endDrawable: Drawable? = null,
            bottomDrawable: Drawable? = null
    ) {
        setDrawables(titleView, startDrawable, topDrawable, endDrawable, bottomDrawable)
    }

    fun setSubTitleDrawables(
            startDrawableId: Int = 0,
            topDrawableId: Int = 0,
            endDrawableId: Int = 0,
            bottomDrawableId: Int = 0
    ) {
        setDrawables(subTitleView, startDrawableId, topDrawableId, endDrawableId, bottomDrawableId)
    }

    fun setSubTitleDrawables(
            startDrawable: Drawable? = null,
            topDrawable: Drawable? = null,
            endDrawable: Drawable? = null,
            bottomDrawable: Drawable? = null
    ) {
        setDrawables(subTitleView, startDrawable, topDrawable, endDrawable, bottomDrawable)
    }
}