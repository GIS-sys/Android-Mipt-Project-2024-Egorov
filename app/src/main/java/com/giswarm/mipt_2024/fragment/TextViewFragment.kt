package com.giswarm.mipt_2024.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager

class TextViewFragment : Fragment(R.layout.fragment_text_view) {
    private val handler: Handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updater: Runnable = object : Runnable {
            override fun run() {
                val textToShow = DevicePositionManager.get().toString() +
                        GpsPositionManager.get().toString()
                view.rootView.findViewById<TextView>(R.id.text_view_text).text = textToShow
                handler.postDelayed(this, 20);
            }
        }
        handler.post(updater)
    }
}