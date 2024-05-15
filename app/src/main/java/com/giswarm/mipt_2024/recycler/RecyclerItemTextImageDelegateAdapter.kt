package com.giswarm.mipt_2024.recycler

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R

class RecyclerItemTextImageDelegateAdapter(val viewActions: ViewTypeDelegateAdapter.OnViewSelectedListener) :
    ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TextImageViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: ViewType,
        isSelected: Boolean,
        position: Int
    ) {
        holder as TextImageViewHolder
        holder.bind(item as RecyclerItemTextImage, isSelected, position)
    }

    inner class TextImageViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.recycler_view_item_text_image)
    ) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageview)
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(item: RecyclerItemTextImage, isSelected: Boolean, position: Int) {
            imageView.setImageDrawable(item.image)
            textView.text = item.text

            super.itemView.setOnClickListener {
                viewActions.onItemSelected(item, position)
            }
            super.itemView.setBackgroundColor(if (isSelected) (0xffff0000).toInt() else (0xff99cc11).toInt())
        }
    }
}