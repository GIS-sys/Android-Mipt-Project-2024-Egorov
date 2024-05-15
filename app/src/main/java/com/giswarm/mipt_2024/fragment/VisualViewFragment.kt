package com.giswarm.mipt_2024.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.hardware.SensorManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.MoonPositionManager
import com.giswarm.mipt_2024.storage.Consts
import com.giswarm.mipt_2024.storage.DrawableManager
import com.giswarm.mipt_2024.usecase.MoonRelativePositionUseCase
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt


const val VISUAL_VIEW_UPDATE_DELAY: Long = 30

class VisualViewFragment : Fragment(R.layout.fragment_visual_view) {
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private val moonRelativePositionUseCase = MoonRelativePositionUseCase()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        Thread {
            while (true) {
                try {
                    Thread.sleep(VISUAL_VIEW_UPDATE_DELAY);
                } catch (e: InterruptedException) {
                    Log.e("DEBUG1305", "interrupted run()");
                }
                try {
                    requireActivity().runOnUiThread {
                        try {
                            draw(view)
                        } catch (_: java.lang.IllegalStateException) {
                        }
                    }
                } catch (_: java.lang.IllegalStateException) {
                }
            }
        }.start()
    }

    private fun draw(view: View) {
        var (moonXraw, moonYraw) = moonRelativePositionUseCase.calculateMoonPosition()

        // screen constants
        val halfSize: Int = (min(screenHeight, screenWidth) * 0.1).toInt()
        val centerX: Int = screenWidth / 2
        val centerY: Int = screenHeight / 2
        val moonX = (moonXraw * min(screenHeight, screenWidth) / 2).toInt()
        val moonY = (moonYraw * min(screenHeight, screenWidth) / 2).toInt()

        // define basic canvas / bitmaps
        val bitmap: Bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)

        // choose and draw moon
        val shapeDrawable = DrawableManager.loadDrawable(DrawableManager.MOON_IMAGE_TAG)
        if (shapeDrawable != null) {
            shapeDrawable.setBounds(
                centerX - halfSize + moonX,
                centerY - halfSize + moonY,
                centerX + halfSize + moonX,
                centerY + halfSize + moonY
            )
            shapeDrawable.draw(canvas)
        }
        if (shapeDrawable == null ||
            requireContext().getSharedPreferences(
                Consts.SHARERD_PRERFERENCES_SETTINGS,
                Context.MODE_PRIVATE
            ).getBoolean(
                Consts.SHARERD_PRERFERENCES_MOON_SHOW_TEXT, false
            )
        ) {
            val paint = Paint()
            paint.textSize = resources.getDimension(R.dimen.text_medium)
            paint.textAlign = Paint.Align.CENTER;
            canvas.drawText(
                getString(R.string.moon),
                centerX.toFloat() + moonX,
                centerY.toFloat() + moonY,
                paint
            )
        }

        // show image
        view.findViewById<LinearLayout>(R.id.visual_view_fragment_layout).background =
            BitmapDrawable(resources, bitmap)
    }
}