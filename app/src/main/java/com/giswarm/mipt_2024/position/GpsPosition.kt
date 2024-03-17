package com.giswarm.mipt_2024.position

import android.content.Context
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class GpsPositionManager(context: Context) {
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

    private var locationManager : LocationManager? = null
    private var lastPosition: GpsPosition = GpsPosition(0.0, 0.0)

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        //locationManager.register
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lastPosition.lng = location.longitude
            lastPosition.lat = location.latitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    fun onPause() {
        // sensorManager.unregisterListener(this)
    }

    fun onResume() {
        // sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }
}