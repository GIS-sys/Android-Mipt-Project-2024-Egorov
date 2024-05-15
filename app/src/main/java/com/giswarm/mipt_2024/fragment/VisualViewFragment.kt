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
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.composable.CanvasMoonComposable
import com.giswarm.mipt_2024.composable.OneTextComposable
import com.giswarm.mipt_2024.dataviewmodel.BitmapModel
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
    private val moonRelativePositionUseCase = MoonRelativePositionUseCase(requireActivity())

    private val bitmapModel = BitmapModel(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        view.rootView.findViewById<ComposeView>(R.id.composable_visual_canvas).setContent{
            CanvasMoonComposable(bitmapModel)
        }

        Thread {
            while (true) {
                try {
                    Thread.sleep(VISUAL_VIEW_UPDATE_DELAY);
                } catch (e: InterruptedException) {
                    Log.e("DEBUG1305", "interrupted sleep");
                }
                try {
                    bitmapModel.image.value = moonRelativePositionUseCase.getBitmap(screenWidth, screenHeight, requireActivity())
                } catch (_: java.lang.IllegalStateException) {
                }
            }
        }.start()
    }
}