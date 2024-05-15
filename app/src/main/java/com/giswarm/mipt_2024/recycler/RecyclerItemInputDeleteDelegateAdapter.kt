package com.giswarm.mipt_2024.recycler

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.giswarm.mipt_2024.R

class RecyclerItemInputDeleteDelegateAdapter(val viewActions: ViewTypeDelegateAdapter.OnViewSelectedListener) :
    ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return InputDeleteViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: ViewType,
        isSelected: Boolean,
        position: Int
    ) {
        holder as InputDeleteViewHolder
        holder.bind(item as RecyclerItemInputDelete)
    }

    inner class InputDeleteViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.recycler_view_item_input_delete)
    ) {

        private val editView: EditText = itemView.findViewById(R.id.editView)
        private val btnView: Button = itemView.findViewById(R.id.btnView)

        fun bind(item: RecyclerItemInputDelete) {
            editView.setText(item.text, TextView.BufferType.EDITABLE)
            btnView.setOnClickListener {
                viewActions.onItemDeleted(item)
            }
        }
    }
}