package com.giswarm.mipt_2024.recycler


open class RecyclerItemInputDelete(val id: String, var text: String) : ViewType {
    override fun getViewType() = AdapterConstants.TEXT_DELETE
}