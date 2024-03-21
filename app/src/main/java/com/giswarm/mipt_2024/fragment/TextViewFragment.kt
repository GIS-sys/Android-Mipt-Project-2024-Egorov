package com.giswarm.mipt_2024.fragment

import android.annotation.SuppressLint
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
            @SuppressLint("SetTextI18n")
            override fun run() {
                val devPos = DevicePositionManager.get()
                view.rootView.findViewById<TextView>(R.id.text_view_text_acc).text = "${getString(R.string.accelerometer)} ${devPos.accX} ${devPos.accY} ${devPos.accZ}"
                view.rootView.findViewById<TextView>(R.id.text_view_text_gyr).text = "${getString(R.string.gyroscope)} ${devPos.gyrX} ${devPos.gyrY} ${devPos.gyrZ}"
                val gpsPos = GpsPositionManager.get()
                view.rootView.findViewById<TextView>(R.id.text_view_text_gps).text = "${getString(R.string.gps)} ${gpsPos.lat} ${gpsPos.lng}"
                handler.postDelayed(this, 20);
            }
        }
        handler.post(updater)
    }
}