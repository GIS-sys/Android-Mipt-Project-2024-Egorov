package com.giswarm.mipt_2024.recycler

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R

class RecyclerItemTextDelegateAdapter(val viewActions: ViewTypeDelegateAdapter.OnViewSelectedListener) :
    ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TextViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: ViewType,
        isSelected: Boolean,
        position: Int
    ) {
        holder as TextViewHolder
        holder.bind(item as RecyclerItemText, isSelected, position)
    }

    inner class TextViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.recycler_view_item_text)
    ) {

        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(item: RecyclerItemText, isSelected: Boolean, position: Int) {
            textView.text = item.text

            super.itemView.setOnClickListener {
                viewActions.onItemSelected(item, position)
            }
            super.itemView.setBackgroundColor(if (isSelected) (0xffff0000).toInt() else (0xff99cc11).toInt())
        }
    }
}