package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.standard.ui.view.keyboard.CustomKeyboardUtils.createKeyBoard
import ru.surfstudio.standard.ui.view.keyboard.controller.EmptyKeyController
import ru.surfstudio.standard.ui.view.keyboard.controller.IconController
import ru.surfstudio.standard.ui.view.keyboard.controller.KeyController
import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey
import ru.surfstudio.standard.ui.view.keyboard.keys.IconKey
import ru.surfstudio.standard.ui.view.keyboard.keys.Key
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

/**
 * View клавиатуры
 */
class KeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {

    var buttonLeft: Key? = null
    var buttonRight: Key? = null

    var onKeyClick: (code: String) -> Unit = {}
    var onDeleteClick = {}

    private val easyAdapter = EasyAdapter().apply { isFirstInvisibleItemEnabled = true }

    private val keyController = KeyController { onKeyClick(it) }
    private val deleteController = IconController { onDeleteClick() }
    private val emptyController = EmptyKeyController()

    init {
        layoutManager = GridLayoutManager(context, KEYBOARD_COLUMNS_COUNT)
        overScrollMode = View.OVER_SCROLL_NEVER
        adapter = easyAdapter

        create()
    }

    private fun create() {
        easyAdapter.setItems(
                ItemList.create().apply {
                    createKeyBoard(buttonLeft, buttonRight).forEach {
                        when (it) {
                            is TextKey -> add(it, keyController)
                            is IconKey -> add(it, deleteController)
                            is EmptyKey -> add(it, emptyController)
                        }
                    }
                }
        )
    }

    companion object {

        const val KEYBOARD_COLUMNS_COUNT = 3
    }
}
