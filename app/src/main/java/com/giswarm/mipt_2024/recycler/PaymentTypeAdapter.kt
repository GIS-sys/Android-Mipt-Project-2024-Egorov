package com.giswarm.mipt_2024.recycler

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
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

class PaymentTypeAdapter(private var recyclerView: RecyclerView, activity: Activity, itemsnew: List<RecyclerItemInputDelete>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class DiffUtilCallback(private val oldList: List<RecyclerItemInputDelete>, private val newList: List<RecyclerItemInputDelete>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private var items: ArrayList<RecyclerItemInputDelete> = ArrayList(itemsnew)
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    var selectedPosition: Int = 0

    // requires at least 1 element
    init {
        if (items.isEmpty()) {
            items.add(RecyclerItemInputDelete("PLACEHOLDER","PLACEHOLDER"))
        }

        val listenerWithSelection = object : ViewTypeDelegateAdapter.OnViewSelectedListener {
            override fun onItemSelected(item: ViewType, position: Int) {
            }

            override fun onItemDeleted(item: ViewType) {
                delete(item as RecyclerItemInputDelete)
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

    fun add(newItems: List<RecyclerItemInputDelete>) {
        setNewData(items + newItems)
    }

    fun delete(item: RecyclerItemInputDelete) {
        var newItems = items.filterNot { it.id == item.id }
        setNewData(newItems)
    }

    fun setNewData(newItems: List<RecyclerItemInputDelete>) {
        val diffCallback = DiffUtilCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}