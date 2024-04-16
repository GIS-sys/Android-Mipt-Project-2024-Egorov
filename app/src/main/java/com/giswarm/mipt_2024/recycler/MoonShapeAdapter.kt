package com.giswarm.mipt_2024.recycler

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView


class MoonShapeAdapter(listener: MoonShapeDelegateAdapter.OnViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.IMAGE_TEXT, MoonShapeDelegateAdapter(listener))
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!.onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun add(newItems: List<MoonShapeItem>) {
        // remove last
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        // add back
        items.addAll(newItems)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    fun clear() {
        val lastSize = itemCount
        items.clear()
        notifyItemRangeRemoved(0, lastSize)
    }

    fun getLoadedItems(): List<MoonShapeItem> = items
            .filter { it.getViewType() == AdapterConstants.IMAGE_TEXT }
            .map { it as MoonShapeItem }
}