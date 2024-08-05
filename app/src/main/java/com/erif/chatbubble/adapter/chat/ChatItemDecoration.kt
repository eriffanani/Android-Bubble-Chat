package com.erif.chatbubble.adapter.chat

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ChatItemDecoration(
    private val list: MutableList<ItemChat>
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val item = list[position]
        if (position < (list.size - 1)) {
            val nextItem = list[position+1]
            if (nextItem.type != item.type)
                with(outRect) {
                    top = 40
                }
            else
                with(outRect) {
                    top = -6
                }
        }
    }

}