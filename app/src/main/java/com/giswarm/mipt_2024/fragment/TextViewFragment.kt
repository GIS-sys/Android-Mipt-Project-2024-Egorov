package com.giswarm.mipt_2024.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.giswarm.mipt_2024.R
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager
import com.giswarm.mipt_2024.position.MoonPositionManager
import com.giswarm.mipt_2024.composable.OneTextComposable
import com.giswarm.mipt_2024.dataviewmodel.OneViewModel
import com.giswarm.mipt_2024.repository.DevicePositionRepository
import com.giswarm.mipt_2024.repository.GpsPositionRepository
import com.giswarm.mipt_2024.repository.MoonPositionRepository


const val UPDATE_DELAY: Long = 50

class TextViewFragment : Fragment(R.layout.fragment_text_view) {
    private val handler: Handler = Handler()
    private lateinit var updater: Runnable

    private var accViewModel = OneViewModel("")
    private var gyrViewModel = OneViewModel("")
    private var comViewModel = OneViewModel("")
    private var gpsViewModel = OneViewModel("")
    private var moonViewModel = OneViewModel("")

    private val devicePositionRepository = DevicePositionRepository()
    private val gpsPositionRepository = GpsPositionRepository()
    private val moonPositionRepository = MoonPositionRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.rootView.findViewById<ComposeView>(R.id.composable_text_acc).setContent{
            OneTextComposable(accViewModel)
        }
        view.rootView.findViewById<ComposeView>(R.id.composable_text_gyr).setContent{
            OneTextComposable(gyrViewModel)
        }
        view.rootView.findViewById<ComposeView>(R.id.composable_text_com).setContent{
            OneTextComposable(comViewModel)
        }
        view.rootView.findViewById<ComposeView>(R.id.composable_text_gps).setContent{
            OneTextComposable(gpsViewModel)
        }
        view.rootView.findViewById<ComposeView>(R.id.composable_text_moon).setContent{
            OneTextComposable(moonViewModel)
        }

        updater = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val devPos = devicePositionRepository.getPosition()
                accViewModel.text.value = "${getString(R.string.accelerometer)} ${devPos.accX} ${devPos.accY} ${devPos.accZ}"
                gyrViewModel.text.value = "${getString(R.string.gyroscope)} ${devPos.gyrX} ${devPos.gyrY} ${devPos.gyrZ}"
                comViewModel.text.value = "${getString(R.string.compass)} ${devPos.degX} ${devPos.degY} ${devPos.degZ}"

                val gpsPos = gpsPositionRepository.getPosition()
                gpsViewModel.text.value = "${getString(R.string.gps)} ${gpsPos.lat} ${gpsPos.lng}"

                val moonPos = moonPositionRepository.getPosition()
                moonViewModel.text.value = "${getString(R.string.moon)} ${moonPos.azimuth} ${moonPos.altitude}"

                handler.postDelayed(this, UPDATE_DELAY)
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