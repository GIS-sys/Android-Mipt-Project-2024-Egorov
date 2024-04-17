package com.giswarm.mipt_2024.recycler

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.giswarm.mipt_2024.R


class MoonShapeAdapter(listener: MoonShapeDelegateAdapter.OnViewSelectedListener, recyclerView: RecyclerView, activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType> = ArrayList()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter { recyclerView.post {
            Log.d("DEBUG_1704", "callback")
            Glide.with(activity).asDrawable()
                .placeholder(R.drawable.mipt_android_icon)
                .error(R.drawable.mipt_android_icon)
                .load("https://goo.gl/gEgYUd")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                        Log.e("DEBUG_1704", "onLoadFailed")
                        //do something if error loading
                        return true
                    }
                    override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                        Log.d("DEBUG_1704", "OnResourceReady")
                        //do something when picture already loaded
                        add(listOf(MoonShapeItem("Item X", p0!!)))
                        return true
                    }
                }).submit()
        }})
        delegateAdapters.put(AdapterConstants.IMAGE_TEXT, MoonShapeDelegateAdapter(listener))
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!.onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun add(newItems: List<MoonShapeItem>) {
        Log.d("DEBUG_1704", "add")
        // remove last
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        // add back
        items.addAll(newItems)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, newItems.size + 1)
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