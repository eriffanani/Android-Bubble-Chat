package com.erif.chatbubble.adapter.chat.line

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erif.bubble.instagram.BubbleInstagram
import com.erif.bubble.line.BubbleLine
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ItemChat

class AdapterChatLine(
    private val list: MutableList<ItemChat>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            HolderOutgoing(inflater.inflate(
                    R.layout.item_chat_line_outgoing, parent, false
            ))
        } else {
            HolderIncoming(inflater.inflate(
                    R.layout.item_chat_line_incoming, parent, false
            ))
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderIncoming) {
            holder.bind(list[position])
        } else if (holder is HolderOutgoing) {
            holder.bind(list[position])
        }
    }

    inner class HolderIncoming(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val bubble: BubbleLine = itemView.findViewById(R.id.item_chat_line_incoming_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_line_incoming_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
        }

    }

    inner class HolderOutgoing(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val bubble: BubbleLine = itemView.findViewById(R.id.item_chat_line_outgoing_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_line_outgoing_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
        }

    }

}