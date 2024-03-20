package com.giswarm.mipt_2024.position

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GpsPositionManager(context: Activity) : LocationListener {
    data class GpsPosition(var lat: Double, var lng: Double)

    companion object {
        private const val READ_ALL_LOCATION_PERMISSION_CODE = 111
        private lateinit var instance: GpsPositionManager

        fun instantiate(context: Activity) {
            instance = GpsPositionManager(context)
        }

        fun onPause() = instance.onPause()

        fun onResume() = instance.onResume()

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
            context: Activity
        ) = instance.onRequestPermissionsResult(requestCode, permissions, grantResults, context)

        fun get(): GpsPosition = instance.lastPosition
    }

    private var locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var lastPosition: GpsPosition = GpsPosition(0.0, 0.0)

    init {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                READ_ALL_LOCATION_PERMISSION_CODE
            )
        }
    }

    private fun initLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0f,
                this
            )
        } catch (ex: SecurityException) {
            Log.e("TIMER_GPS_LOC", "Failed to request location updates")
        }
    }

    override fun onLocationChanged(location: Location) {
        lastPosition.lng = location.longitude
        lastPosition.lat = location.latitude
    }

    fun onPause() {
        locationManager.removeUpdates(this)
    }

    fun onResume() {
        initLocationUpdates()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        context: Activity
    ) {
        if (requestCode == READ_ALL_LOCATION_PERMISSION_CODE) {
            if (grantResults.size == 2 && (grantResults[0] == 0 || grantResults[1] == 0)) {
                initLocationUpdates()
            } else {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                context.finishAffinity()
            }
        }
    }
}