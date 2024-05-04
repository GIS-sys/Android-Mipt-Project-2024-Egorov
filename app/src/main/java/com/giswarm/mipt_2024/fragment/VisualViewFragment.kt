package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.storage.DrawableManager
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.storage.Consts
import kotlin.math.min


class VisualViewFragment : Fragment(R.layout.fragment_visual_view) {
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels
        draw(view)
    }

    private fun draw(view: View) {
        val halfWidth: Int = (min(screenHeight, screenWidth) * 0.2).toInt()
        val halfHeight: Int = (min(screenHeight, screenWidth) * 0.1).toInt()
        val centerX: Int =  screenWidth / 2
        val centerY: Int =  screenHeight / 2

        val bitmap: Bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)
        val shapeDrawable = DrawableManager.loadDrawable(DrawableManager.MOON_IMAGE_TAG)
        if (shapeDrawable != null) {
            shapeDrawable.setBounds(centerX-halfWidth, centerY-halfWidth, centerX+halfWidth, centerY+halfWidth)
            shapeDrawable.draw(canvas)
        }
        if (shapeDrawable == null ||
            requireContext().getSharedPreferences(Consts.SHARERD_PRERFERENCES_SETTINGS, Context.MODE_PRIVATE).getBoolean(
                Consts.SHARERD_PRERFERENCES_MOON_SHOW_TEXT, false)) {
            val paint = Paint()
            paint.textSize = resources.getDimension(R.dimen.text_medium)
            paint.textAlign = Paint.Align.CENTER;
            canvas.drawText(getString(R.string.moon), centerX.toFloat(), centerY.toFloat(), paint)
        }
        view.findViewById<LinearLayout>(R.id.visual_view_fragment_layout).background = BitmapDrawable(resources, bitmap)
    }
}