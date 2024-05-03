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


class MoonShapeAdapter(listener: RecyclerItemTextImageDelegateAdapter.OnViewSelectedListener, recyclerView: RecyclerView, activity: Activity, itemsnew: List<ViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType> = ArrayList(itemsnew)
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    private val lock = Any()

    // requires at least 1 element
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
                        add(listOf(RecyclerItemTextImage("Item "+items.size.toString(), p0!!)))
                        return true
                    }
                }).submit()
        }})
        delegateAdapters.put(AdapterConstants.IMAGE_TEXT, RecyclerItemTextImageDelegateAdapter(listener))
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!.onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun add(newItems: List<ViewType>) {
        Log.d("DEBUG_1704", "add b" + items.size.toString())
        synchronized(lock) {
            Log.d("DEBUG_1704", "add ma" + items.size.toString())
            // remove last
            val initPosition = items.size - 1
            items.removeAt(initPosition)
            Log.d("DEBUG_1704", "add mb" + items.size.toString())
            //notifyItemRemoved(initPosition)
            Log.d("DEBUG_1704", "add mc" + items.size.toString())
            // add back
            items.addAll(newItems)
            Log.d("DEBUG_1704", "add md" + items.size.toString())
            items.add(loadingItem)
            Log.d("DEBUG_1704", "add me" + items.size.toString() + "|" + initPosition.toString() + "|" + newItems.size.toString())
            notifyItemRangeChanged(initPosition, newItems.size + 1)
            notifyItemRangeInserted(initPosition, newItems.size + 1)
            Log.d("DEBUG_1704", "add mf" + items.size.toString())
        }
        Log.d("DEBUG_1704", "add a" + items.size.toString())
    }

    fun clear() {
        val lastSize = itemCount
        synchronized(lock) {
            items.clear()
            notifyItemRangeRemoved(0, lastSize)
        }
    }

    fun getLoadedItems(): List<ViewType> = items
            .filter { it.getViewType() == AdapterConstants.IMAGE_TEXT }
            .map { it as RecyclerItemTextImage }
}