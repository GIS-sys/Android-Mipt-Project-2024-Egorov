package com.giswarm.mipt_2024.recycler

import android.graphics.drawable.Drawable


open class RecyclerItemTextImage(val text: String, val image: Drawable) : ViewType {
    override fun getViewType() = AdapterConstants.IMAGE_TEXT
}