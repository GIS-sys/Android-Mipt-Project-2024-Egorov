package com.giswarm.mipt_2024.recycler

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.giswarm.mipt_2024.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException

class PaymentTypeAdapter(listener: ViewTypeDelegateAdapter.OnViewSelectedListener, private var recyclerView: RecyclerView, activity: Activity, itemsnew: List<ViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType> = ArrayList(itemsnew)
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    var selectedPosition: Int = 0

    // requires at least 1 element
    init {
        if (items.isEmpty()) {
            items.add(RecyclerItemText("PLACEHOLDER"))
        }

        val listenerWithSelection = object : ViewTypeDelegateAdapter.OnViewSelectedListener {
            override fun onItemSelected(item: ViewType, position: Int) {
                val lastSelected = selectedPosition
                selectedPosition = position
                listener.onItemSelected(item, position)
                notifyItemChanged(position)
                notifyItemChanged(lastSelected)
            }
        }
        delegateAdapters.put(
            AdapterConstants.TEXT_DELETE,
            RecyclerItemInputDeleteDelegateAdapter(listenerWithSelection)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegateAdapters[viewType]!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters[getItemViewType(position)]!!.onBindViewHolder(holder, items[position], false, 0)
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun add(newItems: List<ViewType>) {
        items.addAll(newItems)
        recyclerView.post {
            notifyItemRangeInserted(items.size - newItems.size, newItems.size)
        }
    }

    fun clear() {
        val lastSize = itemCount
        items.clear()
        notifyItemRangeRemoved(0, lastSize)
    }
}