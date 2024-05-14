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

    private fun calculateMoonPosition(): Pair<Double, Double> {
        // get data
        val pos = (requireActivity() as DevicePositionManager).getDevicePosition()
        val moonPos = (requireActivity() as MoonPositionManager).getMoonPosition()
        val x: Double
        val y: Double

        // device position
        val mAccelerometerData = FloatArray(3)
        mAccelerometerData[0] = pos.accX.toFloat()
        mAccelerometerData[1] = pos.accY.toFloat()
        mAccelerometerData[2] = pos.accZ.toFloat()
        val mMagnetometerData = FloatArray(3)
        mMagnetometerData[0] = pos.degX.toFloat()
        mMagnetometerData[1] = pos.degY.toFloat()
        mMagnetometerData[2] = pos.degZ.toFloat()
        val mMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(mMatrix, null, mAccelerometerData, mMagnetometerData)

        // moon vector
        val moonVector = FloatArray(3)
        moonVector[0] = (sin(moonPos.altitude / 180 * PI)).toFloat()
        moonVector[1] = (cos(moonPos.altitude / 180 * PI) * sin(moonPos.azimuth / 180 * PI)).toFloat()
        moonVector[2] = (cos(moonPos.altitude / 180 * PI) * cos(moonPos.azimuth / 180 * PI)).toFloat()

        // resulting vector
        val resVector = FloatArray(3)
        resVector[0] =
            mMatrix[0] * moonVector[0] + mMatrix[3] * moonVector[1] + mMatrix[6] * moonVector[2]
        resVector[1] =
            mMatrix[1] * moonVector[0] + mMatrix[4] * moonVector[1] + mMatrix[7] * moonVector[2]
        resVector[2] =
            mMatrix[2] * moonVector[0] + mMatrix[5] * moonVector[1] + mMatrix[8] * moonVector[2]
        val resAbs =
            sqrt(resVector[0] * resVector[0] + resVector[1] * resVector[1] + resVector[2] * resVector[2])
        x = -(resVector[0] / resAbs).toDouble()
        y = (resVector[1] / resAbs).toDouble()
        val z = (resVector[1] / resAbs).toDouble()

        Log.d("DEBUG_MOON_POS", "$x $y $z")

        // return
        return Pair(x, y)
    }

    private fun draw(view: View) {
        var (moonXraw, moonYraw) = calculateMoonPosition()

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