package com.giswarm.mipt_2024

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.fragment.GreetingsFragment
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)

        // DEBUG just for testing intents
        /*if (intent.data?.scheme.isNullOrBlank() && intent.data?.host.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_VIEW)
            // intent.data = Uri.parse("com.giswarm.mipt_2024://main.text")
            // intent.data = Uri.parse("com.giswarm.mipt_2024://main.visual")
            //intent.data = Uri.parse("com.giswarm.mipt_2024://credentials")
            intent.data = Uri.parse("com.giswarm.mipt_2024://settings")
            // intent.data = Uri.parse("com.giswarm.mipt_2024://NOTEXISTS")
            startActivity(intent)
            return;
        }*/
        // OR use "deeplink tester" app in google play to test this

        IntentProcessor.process(intent)

        DevicePositionManager.instantiate(this)
        GpsPositionManager.instantiate(this)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.root_fragment_container_view, GreetingsFragment())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        DevicePositionManager.onPause()
        GpsPositionManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        DevicePositionManager.onResume()
        GpsPositionManager.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        GpsPositionManager.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}