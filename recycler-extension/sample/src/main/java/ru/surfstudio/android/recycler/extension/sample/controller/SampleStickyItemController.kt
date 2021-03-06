package ru.surfstudio.android.recycler.extension.sample.controller

import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.sample.R
import ru.surfstudio.android.recycler.extension.sample.domain.Data
import ru.surfstudio.android.recycler.extension.sticky.controller.StickyHeaderBindableItemController
import ru.surfstudio.android.recycler.extension.sticky.item.StickyHeaderBindableItem

class SampleStickyItemController(
        private val isFullScreen: Boolean = false
) : StickyHeaderBindableItemController<Data, SampleStickyItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup?): Holder = Holder(parent)

    inner class Holder(
            parent: ViewGroup?
    ) : BindableViewHolder<Data>(parent, R.layout.item_list_header_sample) {

        private var textView: TextView = itemView.findViewById(R.id.item_list_sample_header_tv)

        init {
            if (isFullScreen) {
                textView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }

        override fun bind(data: Data?) {
            textView.text = data?.name
        }
    }
}