package com.giswarm.mipt_2024.position

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DevicePositionManager(context: Context) : SensorEventListener {
    data class DevicePosition(
        var accX: Double,
        var accY: Double,
        var accZ: Double,
        var gyrX: Double,
        var gyrY: Double,
        var gyrZ: Double
    )

    companion object {
        private lateinit var instance: DevicePositionManager

        fun instantiate(context: Context) {
            instance = DevicePositionManager(context)
        }

        fun onPause() = instance.onPause()

        fun onResume() = instance.onResume()

        fun get(): DevicePosition = instance.lastPosition
    }

    private val sensorManager: SensorManager
    private val accelerometer: Sensor?
    private val gyroscope: Sensor?
    private var lastPosition: DevicePosition = DevicePosition(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            lastPosition.accX = event.values[0].toDouble()
            lastPosition.accY = event.values[1].toDouble()
            lastPosition.accZ = event.values[2].toDouble()
        }
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            lastPosition.gyrX = event.values[0].toDouble()
            lastPosition.gyrY = event.values[1].toDouble()
            lastPosition.gyrZ = event.values[2].toDouble()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun onPause() {
        sensorManager.unregisterListener(this)
    }

    fun onResume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
    }
}