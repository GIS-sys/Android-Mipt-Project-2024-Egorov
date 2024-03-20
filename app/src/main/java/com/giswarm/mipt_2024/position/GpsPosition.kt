package com.giswarm.mipt_2024.position

import android.content.Context
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class GpsPositionManager(context: Context): LocationListener {
    data class GpsPosition(var lat: Double, var lng: Double)

    companion object {
        private lateinit var instance: GpsPositionManager

        fun instantiate(context: Context) {
            instance = GpsPositionManager(context)
        }

        fun onPause() = instance.onPause()

        fun onResume() = instance.onResume()

        fun get(): GpsPosition = instance.lastPosition
    }

    private var locationManager : LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    private var lastPosition: GpsPosition = GpsPosition(0.0, 0.0)

    init {
        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1f,
                this
            )
        } catch (ex: SecurityException) {
            // Handle security exception
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("TIMER_GPS_LOC", "location changed")
        lastPosition.lng = location.longitude
        lastPosition.lat = location.latitude
    }

    fun onPause() {
        // sensorManager.unregisterListener(this)
    }

    fun onResume() {
        // sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }
}