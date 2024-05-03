package com.giswarm.mipt_2024.recycler

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.giswarm.mipt_2024.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException


class MoonShapeAdapter(listener: RecyclerItemTextImageDelegateAdapter.OnViewSelectedListener, recyclerView: RecyclerView, activity: Activity, itemsnew: List<ViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType> = ArrayList(itemsnew)
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    private val lock = Any()
    private val imageLinks: ArrayList<String> = ArrayList()

    // requires at least 1 element
    init {
        val request = Request.Builder()
            .url("https://emojiguide.org/smileys-and-emotion")
            .build()
        OkHttpClient().newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("DEBUG_1704", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()
                    val document = Jsoup.parse(responseBody)
                    val element = document.getElementById("content")
                    for (innerElement in element.children()) {
                        imageLinks.add(innerElement.attr("href"))
                    }
                }
            }
        )

        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter { recyclerView.post {
            val request = Request.Builder()
                .url("https://emojiguide.org/" + imageLinks.elementAtOrElse(items.size) { _ -> "/grinning-face" })
                .build()
            OkHttpClient().newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("DEBUG_1704", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body?.string()
                        val document = Jsoup.parse(responseBody)
                        val element = document.getElementById("pictures-list")
                        val imageSrc = "https://emojiguide.org" + element.child(0).child(0).attr("src")
                        Log.d("DEBUG_1704", imageSrc)
                        Glide.with(activity).asDrawable()
                            .placeholder(R.drawable.mipt_android_icon)
                            .error(R.drawable.mipt_android_icon)
                            .load(imageSrc)
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
                    }
                }
            )
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
        synchronized(lock) {
            // remove last
            val initPosition = items.size - 1
            items.removeAt(initPosition)
            // add back
            items.addAll(newItems)
            items.add(loadingItem)
            notifyItemRangeChanged(initPosition, newItems.size + 1)
            notifyItemRangeInserted(initPosition, newItems.size + 1)
        }
    }

    fun clear() {
        val lastSize = itemCount
        synchronized(lock) {
            items.clear()
            notifyItemRangeRemoved(0, lastSize)
        }
    }

    /*
    fun getLoadedItems(): List<ViewType> = items
            .filter { it.getViewType() == AdapterConstants.IMAGE_TEXT }
            .map { it as RecyclerItemTextImage }
    */
}