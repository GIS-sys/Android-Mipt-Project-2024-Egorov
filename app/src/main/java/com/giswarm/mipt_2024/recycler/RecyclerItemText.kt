package com.giswarm.mipt_2024.recycler

import android.graphics.drawable.Drawable


open class RecyclerItemText(val text: String) : ViewType {
    override fun getViewType() = AdapterConstants.TEXT
}