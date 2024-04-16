package com.giswarm.mipt_2024

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.fragment.CredentialsFragment
import com.giswarm.mipt_2024.fragment.GreetingsFragment
import com.giswarm.mipt_2024.fragment.MainFragment
import com.giswarm.mipt_2024.fragment.SettingsFragment
import com.giswarm.mipt_2024.position.DevicePosition
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.GpsPosition
import com.giswarm.mipt_2024.position.GpsPositionManager
import com.giswarm.mipt_2024.position.MoonPosition
import com.giswarm.mipt_2024.position.MoonPositionManager

const val READ_ALL_LOCATION_PERMISSION_CODE = 111

class MainActivity : AppCompatActivity(), SensorEventListener, LocationListener, DevicePositionManager, GpsPositionManager, MoonPositionManager {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var devicePosition: DevicePosition = DevicePosition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    private lateinit var locationManager: LocationManager
    private var gpsPosition: GpsPosition = GpsPosition(0.0, 0.0)

    private var moonPosition: MoonPosition = MoonPosition(0.0, 0.0)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            devicePosition.accX = event.values[0].toDouble()
            devicePosition.accY = event.values[1].toDouble()
            devicePosition.accZ = event.values[2].toDouble()
        }
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            devicePosition.gyrX = event.values[0].toDouble()
            devicePosition.gyrY = event.values[1].toDouble()
            devicePosition.gyrZ = event.values[2].toDouble()
        }
    }

    override fun onLocationChanged(location: Location) {
        gpsPosition.lng = location.longitude
        gpsPosition.lat = location.latitude
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

    override fun getDevicePosition(): DevicePosition {
        return devicePosition
    }

    override fun getGpsPosition(): GpsPosition {
        return gpsPosition
    }

    override fun getMoonPosition(): MoonPosition {
        return moonPosition
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                READ_ALL_LOCATION_PERMISSION_CODE
            )
        }

        // DEBUG just for testing intents
        /*if (intent.data?.scheme.isNullOrBlank() && intent.data?.host.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_VIEW)
            // intent.data = Uri.parse("com.giswarm.mipt_2024://main.text")
            // intent.data = Uri.parse("com.giswarm.mipt_2024://main.visual")
            intent.data = Uri.parse("com.giswarm.mipt_2024://credentials")
            // intent.data = Uri.parse("com.giswarm.mipt_2024://settings")
            // intent.data = Uri.parse("com.giswarm.mipt_2024://NOTEXISTS")
            startActivity(intent)
            return;
        }*/
        // OR use "deeplink tester" app in google play to test this

        IntentProcessor.process(this, intent)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.root_fragment_container_view, GreetingsFragment())
            }
        }
    }

    fun moveToMain() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, MainFragment())
        }
    }

    fun moveToCredentials() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, CredentialsFragment())
            addToBackStack(null)
        }
    }

    fun moveToSettings() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_fragment_container_view, SettingsFragment())
            addToBackStack(null)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
        initLocationUpdates()
    }
}