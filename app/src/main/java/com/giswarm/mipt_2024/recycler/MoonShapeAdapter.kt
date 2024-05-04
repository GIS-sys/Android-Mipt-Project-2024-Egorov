package com.giswarm.mipt_2024.recycler

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.core.content.ContextCompat.getDrawable
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


// links for loading items
private const val URL_IMAGE_HOST = "https://emojiguide.org/"
private const val URL_IMAGE_HUB = "${URL_IMAGE_HOST}smileys-and-emotion"
private const val URL_IMAGE_DEFAULT = "grinning-face"


class MoonShapeAdapter(listener: ViewTypeDelegateAdapter.OnViewSelectedListener, recyclerView: RecyclerView, activity: Activity, itemsnew: List<ViewType>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ViewType> = ArrayList(itemsnew)
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    // only single loading item allowed
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    // for infinite load we need lock (for sync) and array of downloaded images
    private val lock = Any()
    private val imageLinks: ArrayList<String> = ArrayList()

    // requires at least 1 element
    init {
        if (items.isEmpty()) {
            items.add(RecyclerItemText("PLACEHOLDER"))
        }

        OkHttpClient().newCall(
            Request.Builder().url(URL_IMAGE_HUB).build()
        ).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("DEBUG_1704", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val document = Jsoup.parse(response.body?.string())
                    val elementListLinks = document.getElementById("content")
                    for (innerElement in elementListLinks.children()) {
                        imageLinks.add(innerElement.attr("href"))
                    }
                }
            }
        )

        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter { recyclerView.post {
            OkHttpClient().newCall(
                Request.Builder()
                    .url(URL_IMAGE_HOST + imageLinks.elementAtOrElse(items.size) { _ -> URL_IMAGE_DEFAULT })
                    .build()
            ).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("DEBUG_1704", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val document = Jsoup.parse(response.body?.string())
                        val elementImages = document.getElementById("pictures-list")
                        val imageSrcPart = elementImages.child(0).child(0).attr("src")
                        val imageSrc = URL_IMAGE_HOST + imageSrcPart
                        Glide.with(activity).asDrawable()
                            .placeholder(R.drawable.mipt_android_icon)
                            .error(R.drawable.mipt_android_icon)
                            .load(imageSrc)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                                    Log.e("DEBUG_1704", "onLoadFailed")
                                    add(listOf(RecyclerItemTextImage(imageSrcPart, getDrawable(activity.applicationContext, R.drawable.mipt_android_icon)!!)))
                                    return true
                                }
                                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                                    add(listOf(RecyclerItemTextImage(imageSrcPart, p0!!)))
                                    return true
                                }
                            }).submit()
                    }
                }
            )
        }})
        delegateAdapters.put(AdapterConstants.IMAGE_TEXT, RecyclerItemTextImageDelegateAdapter(listener))
        delegateAdapters.put(AdapterConstants.TEXT, RecyclerItemTextDelegateAdapter(listener))
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
}