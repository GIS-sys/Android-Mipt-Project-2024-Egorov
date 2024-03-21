package com.giswarm.mipt_2024.fragment

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat.OrientationMode
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R

class VisualViewFragment : Fragment(R.layout.fragment_visual_view) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        draw(view, resources.configuration.orientation == 2)
    }

    private fun draw(view: View, isLandscape: Boolean) {
        val left = if (isLandscape) 0 else 100
        val top = if (isLandscape) 500 else 500
        val right = if (isLandscape) 600 else 600
        val bottom = if (isLandscape) 800 else 800
        val width = if (isLandscape) 1000 else 700
        val height = if (isLandscape) 700 else 1000

        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)
        val shapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.setBounds( left, top, right, bottom)
        shapeDrawable.paint.color = Color.parseColor("#009191")
        shapeDrawable.draw(canvas)
        view.findViewById<LinearLayout>(R.id.visual_view_fragment_layout).background = BitmapDrawable(resources, bitmap)
    }
}