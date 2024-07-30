package com.erif.chatbubble.adapter.chat.whatsapp.ios

import android.text.StaticLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erif.bubble.whatsapp.BubbleWhatsapp
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ItemChat

class AdapterChatWhatsappIOS(
    private val list: MutableList<ItemChat>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            HolderOutgoing(inflater.inflate(
                R.layout.item_chat_wa_ios_outgoing, parent, false
            ))
        } else {
            HolderIncoming(inflater.inflate(
                R.layout.item_chat_wa_ios_incoming, parent, false
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

        private val bubble: BubbleWhatsapp = itemView.findViewById(R.id.item_chat_wa_ios_incoming_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_wa_ios_incoming_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
            item.message?.let {
                val maxWidthMessage = txt.maxWidth
                val layoutMessage = StaticLayout.Builder
                    .obtain(it, 0, it.length, txt.paint, maxWidthMessage)
                    .setLineSpacing(txt.lineSpacingExtra, txt.lineSpacingMultiplier)
                    .setMaxLines(txt.maxLines)
                    .build()
                val lineCount = layoutMessage.lineCount
                val param = txt.layoutParams as FrameLayout.LayoutParams
                if (lineCount > 1) {
                    var maxLineWidth = 0f
                    for (i in 0 until lineCount) {
                        val lineWidth = layoutMessage.getLineWidth(i)
                        if (lineWidth > maxLineWidth)
                            maxLineWidth = lineWidth
                    }

                    param.width = (maxLineWidth + 1f).toInt()
                } else {
                    param.width = FrameLayout.LayoutParams.WRAP_CONTENT
                }
                txt.layoutParams = param
            }
        }

    }

    inner class HolderOutgoing(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val bubble: BubbleWhatsapp = itemView.findViewById(R.id.item_chat_wa_ios_outgoing_bubble)
        private val txt: TextView = itemView.findViewById(R.id.item_chat_wa_ios_outgoing_txt)

        fun bind(item: ItemChat) {
            txt.text = item.message
            bubble.setBubbleCondition(item.condition)
            item.message?.let {
                val maxWidthMessage = txt.maxWidth
                val layoutMessage = StaticLayout.Builder
                    .obtain(it, 0, it.length, txt.paint, maxWidthMessage)
                    .setLineSpacing(txt.lineSpacingExtra, txt.lineSpacingMultiplier)
                    .setMaxLines(txt.maxLines)
                    .build()
                val lineCount = layoutMessage.lineCount
                val param = txt.layoutParams as FrameLayout.LayoutParams
                if (lineCount > 1) {
                    var maxLineWidth = 0f
                    for (i in 0 until lineCount) {
                        val lineWidth = layoutMessage.getLineWidth(i)
                        if (lineWidth > maxLineWidth)
                            maxLineWidth = lineWidth
                    }
                    param.width = (maxLineWidth + 1f).toInt()
                } else {
                    param.width = FrameLayout.LayoutParams.WRAP_CONTENT
                }
                txt.layoutParams = param
            }
        }

    }

}