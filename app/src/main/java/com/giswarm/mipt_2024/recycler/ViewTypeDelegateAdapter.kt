package com.giswarm.mipt_2024.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewTypeDelegateAdapter {
    interface OnViewSelectedListener {
        fun onItemSelected(item: ViewType, position: Int)
        fun onItemDeleted(item: ViewType)
    }

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: ViewType,
        isSelected: Boolean,
        position: Int
    )
}