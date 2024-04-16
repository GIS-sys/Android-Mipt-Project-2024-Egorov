package com.giswarm.mipt_2024.recycler

import android.os.Parcel
import android.os.Parcelable


data class MoonShapeItem(val text: String, val image: Int) : ViewType {
    override fun getViewType() = AdapterConstants.IMAGE_TEXT
}
/*data class MoonShapeItem(
    val author: String,
    val title: String,
    val numComments: Int,
    val created: Long,
    val thumbnail: String,
    val url: String?
) : ViewType, Parcelable {

    override fun getViewType() = AdapterConstants.IMAGE_TEXT

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<MoonShapeItem> = object : Parcelable.Creator<MoonShapeItem> {
            override fun createFromParcel(source: Parcel): MoonShapeItem = MoonShapeItem(source)
            override fun newArray(size: Int): Array<MoonShapeItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString()!!,
        source.readString()!!, source.readInt(), source.readLong(), source.readString()!!, source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(author)
        dest.writeString(title)
        dest.writeInt(numComments)
        dest.writeLong(created)
        dest.writeString(thumbnail)
        dest.writeString(url)
    }
}*/