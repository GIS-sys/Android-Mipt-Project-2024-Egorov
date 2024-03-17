package com.giswarm.mipt_2024

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.giswarm.mipt_2024.fragment.GreetingsFragment
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException


class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_layout)
        Log.d("SENSOR", "start")
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.root_fragment_container_view, GreetingsFragment())
            }
        }
        Log.d("SENSORSTART", "start")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val client = OkHttpClient()

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            Log.d("SENSOR", "X: $x, Y: $y, Z: $z")

            val formBody = FormBody.Builder()
                .add("x", "$x")
                .add("y", "$y")
                .add("z", "$z")
                .build()
            val request = Request.Builder()
                //.url("http://192.168.1.18:8080/new_data")
                .url("http://postman-echo.com/post")
                .post(formBody)
                .build()

            try {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        Log.d("SENSORNET", "exception $e")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) Log.d("SENSORNET", "Unexpected code $response")
                        else Log.e("SENSORNET", response.body!!.string())
                    }
                })
            } catch (e: Exception) {
                Log.e("SENSORNET", e.toString())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }
}