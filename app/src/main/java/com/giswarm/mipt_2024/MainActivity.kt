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
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import com.giswarm.mipt_2024.repository.DevicePositionRepository
import com.giswarm.mipt_2024.repository.GpsPositionRepository
import com.giswarm.mipt_2024.repository.MoonPositionRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

const val READ_ALL_LOCATION_PERMISSION_CODE = 111

class MainActivity : AppCompatActivity(), SensorEventListener, LocationListener,
    DevicePositionManager, GpsPositionManager, MoonPositionManager {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var compass: Sensor? = null
    private lateinit var locationManager: LocationManager

    private val timerMoon = Timer()

    private val devicePositionRepository = DevicePositionRepository()
    private val moonPositionRepository = MoonPositionRepository()
    private val gpsPositionRepository = GpsPositionRepository()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            devicePositionRepository.setAcc(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            )
        }
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            devicePositionRepository.setGyr(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            )
        }
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            devicePositionRepository.setCom(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        gpsPositionRepository.setPosition(GpsPosition(location.latitude, location.longitude))
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
        return devicePositionRepository.getPosition()
    }

    override fun getGpsPosition(): GpsPosition {
        return gpsPositionRepository.getPosition()
    }

    override fun getMoonPosition(): MoonPosition {
        return moonPositionRepository.getPosition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_UI)

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

        timerMoon.schedule(object : TimerTask() {
            override fun run() {
                OkHttpClient().newCall(
                    Request.Builder()
                        // REMOVE TMP FROM KEY FOR REAL TEST - for now wrong api key to not waste limits
                        .url(
                            "https://api.ipgeolocation.io/astronomy?apiKey=fc95889c2ce94d59900262967d16113cTMP&" +
                                    "lat=${gpsPositionRepository.getPosition().lat}&" +
                                    "long=${gpsPositionRepository.getPosition().lng}"
                        )
                        .build()
                ).enqueue(
                    object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("DEBUG_1704", e.toString())
                        }

                        override fun onResponse(call: Call, response: Response) {
                            response.body?.string()?.let {
                                val moonPos: Map<String, Any?> =
                                    jacksonObjectMapper().readValue(it)
                                moonPos["moon_altitude"]?.let { alt ->
                                    moonPos["moon_azimuth"]?.let { azi ->
                                        {
                                            moonPositionRepository.setPosition(
                                                MoonPosition(
                                                    alt.toString().toDouble(),
                                                    azi.toString().toDouble()
                                                )
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                )
            }
        }, 5000, 60000)
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