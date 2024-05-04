package com.giswarm.mipt_2024.storage

import android.graphics.drawable.Drawable

object DrawableManager {
    const val MOON_IMAGE_TAG = "MOON_IMAGE_TAG"

    private var imagesStorage: MutableMap<String, Drawable?> = mutableMapOf()

    fun saveDrawable(image: Drawable?, tag: String) {
        imagesStorage[tag] = image
    }

    fun loadDrawable(tag: String): Drawable? {
        return imagesStorage[tag]
    }
}