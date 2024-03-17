package com.giswarm.mipt_2024

import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.fragment.GreetingsFragment
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPositionManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)

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
    }

    override fun onResume() {
        super.onResume()
        DevicePositionManager.onResume()
    }
}