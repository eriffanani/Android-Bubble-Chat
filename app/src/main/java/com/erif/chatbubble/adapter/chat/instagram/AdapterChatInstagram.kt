package com.erif.chatbubble.adapter.chat.instagram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erif.bubble.instagram.BubbleInstagram
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ItemChat

class AdapterChatInstagram(
    private val list: MutableList<ItemChat>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            HolderOutgoing(inflater.inflate(
                    R.layout.item_chat_ig_outgoing, parent, false
            ))
        } else {
            HolderIncoming(inflater.inflate(
                    R.layout.item_chat_ig_incoming, parent, false
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

        private val bubble: BubbleInstagram = itemView.findViewById(R.id.item_chat_ig_incoming_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_ig_incoming_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
        }

    }

    inner class HolderOutgoing(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val bubble: BubbleInstagram = itemView.findViewById(R.id.item_chat_ig_outgoing_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_ig_outgoing_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
        }

    }

}