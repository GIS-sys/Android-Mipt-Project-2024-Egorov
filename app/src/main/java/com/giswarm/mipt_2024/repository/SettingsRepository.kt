package com.giswarm.mipt_2024.repository

import android.content.Context
import android.graphics.drawable.Drawable
import com.giswarm.mipt_2024.storage.Consts
import com.giswarm.mipt_2024.storage.DrawableManager

class SettingsRepository(/*val context: Context*/) {
    companion object {
        @Volatile
        private var isText: Boolean? = null
    }

    fun setShowText(newIsText: Boolean) {
        isText = newIsText
//        context.getSharedPreferences(
//            Consts.SHARERD_PRERFERENCES_SETTINGS,
//            Context.MODE_PRIVATE
//        ).edit().putBoolean(Consts.SHARERD_PRERFERENCES_MOON_SHOW_TEXT, newIsText)
//            .apply()
    }

    fun isShowText(): Boolean {
        return isText ?: false /*?: context.getSharedPreferences(
            Consts.SHARERD_PRERFERENCES_SETTINGS,
            Context.MODE_PRIVATE
        ).getBoolean(
            Consts.SHARERD_PRERFERENCES_MOON_SHOW_TEXT, false
        )*/
    }

    fun setMoonShape(drawable: Drawable?) {
        DrawableManager.saveDrawable(drawable, DrawableManager.MOON_IMAGE_TAG)
    }

    fun getMoonShape(): Drawable? {
        return DrawableManager.loadDrawable(DrawableManager.MOON_IMAGE_TAG)
    }
}