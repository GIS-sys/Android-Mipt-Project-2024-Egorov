package com.giswarm.mipt_2024.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.DebugSendData
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager
import com.giswarm.mipt_2024.position.MoonPositionManager


const val UPDATE_DELAY: Long = 50
class TextViewFragment : Fragment(R.layout.fragment_text_view) {
    private val handler: Handler = Handler()
    private lateinit var updater: Runnable

    private lateinit var textAcc: TextView
    private lateinit var textGyr: TextView
    private lateinit var textGps: TextView
    private lateinit var textMoon: TextView

    private lateinit var debugIp: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textAcc = view.rootView.findViewById<TextView>(R.id.text_view_text_acc)
        textGyr = view.rootView.findViewById<TextView>(R.id.text_view_text_gyr)
        textGps = view.rootView.findViewById<TextView>(R.id.text_view_text_gps)
        textMoon = view.rootView.findViewById<TextView>(R.id.text_view_text_moon)
        //debugIp = view.rootView.findViewById<EditText>(R.id.edit_text_url)

        updater = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val devPos = (activity as DevicePositionManager).getDevicePosition()
                textAcc.text = "${getString(R.string.accelerometer)} ${devPos.accX} ${devPos.accY} ${devPos.accZ}"
                textGyr.text = "${getString(R.string.gyroscope)} ${devPos.gyrX} ${devPos.gyrY} ${devPos.gyrZ}"
                val gpsPos = (activity as GpsPositionManager).getGpsPosition()
                textGps.text = "${getString(R.string.gps)} ${gpsPos.lat} ${gpsPos.lng}"
                val moonPos = (activity as MoonPositionManager).getMoonPosition()
                textMoon.text = "${getString(R.string.moon)} ${moonPos.azimuth} ${moonPos.altitude}"
                //DebugSendData.send(devPos, debugIp.text.toString())
                handler.postDelayed(this, UPDATE_DELAY);
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        handler.post(updater)
    }
}