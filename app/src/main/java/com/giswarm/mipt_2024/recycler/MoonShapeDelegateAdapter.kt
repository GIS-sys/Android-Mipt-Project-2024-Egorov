package com.giswarm.mipt_2024.recycler

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R

class MoonShapeDelegateAdapter(val viewActions: OnViewSelectedListener) : ViewTypeDelegateAdapter {
    interface OnViewSelectedListener {
        fun onItemSelected(item: MoonShapeItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return MoonShapeViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as MoonShapeViewHolder
        holder.bind(item as MoonShapeItem)
    }

    inner class MoonShapeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.recycler_view_moon_shape)) {

        private val imageView: ImageView = itemView.findViewById(R.id.imageview)
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(item: MoonShapeItem) {
            imageView.setImageDrawable(item.image)
            textView.text = item.text
            /*holder.itemView.setOnClickListener{
                val lastSelected = selectedPos
                selectedPos = position
                onClickListener.invoke(position)
                notifyItemChanged(lastSelected)
                notifyItemChanged(position)
            }*/
            //holder.itemView.setBackgroundColor(if (selectedPos == position) (0xff000000).toInt() else (0xff99cc11u).toInt())

            super.itemView.setOnClickListener { viewActions.onItemSelected(item)}
        }
    }
}