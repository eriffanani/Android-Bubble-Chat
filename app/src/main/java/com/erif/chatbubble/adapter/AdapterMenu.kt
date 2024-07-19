package com.erif.chatbubble.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erif.chatbubble.R

class AdapterMenu(
    private val list: MutableList<ItemMenu>,
    private val listener: OnClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_social_media, parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.bind(list[position], position)
        }
    }

    inner class Holder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val line: View = itemView.findViewById(R.id.item_menu_line)
        private val icon: ImageView = itemView.findViewById(R.id.item_menu_imgIcon)
        private val txtName: TextView = itemView.findViewById(R.id.item_menu_txtName)

        fun bind(item: ItemMenu, position: Int) {
            line.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            icon.setImageResource(item.icon)
            txtName.text = item.name
            itemView.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(item: ItemMenu)
    }

}