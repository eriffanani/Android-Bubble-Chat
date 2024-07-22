package com.erif.chatbubble.adapter.chat.whatsapp.ios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ItemChat

class AdapterChatWhatsappIOS(
    private val list: MutableList<ItemChat>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            HolderOutgoing(inflater.inflate(
                    R.layout.item_chat_wa_android_outgoing, parent, false
            ))
        } else {
            HolderIncoming(inflater.inflate(
                    R.layout.item_chat_wa_android, parent, false
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

        fun bind(item: ItemChat) {

        }

    }

    inner class HolderOutgoing(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ItemChat) {

        }

    }

}