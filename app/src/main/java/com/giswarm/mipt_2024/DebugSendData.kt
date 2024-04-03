package com.giswarm.mipt_2024

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
import com.giswarm.mipt_2024.position.DevicePosition
import com.giswarm.mipt_2024.position.DevicePositionManager
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

class DebugSendData {
    companion object {
        fun send(lastDevicePosition: DevicePosition, ip: String) {
            Log.d("SENSOR", lastDevicePosition.toString())
            var jsonObject = JSONObject()
            try {
                jsonObject.put("ax", "${lastDevicePosition.accX}")
                jsonObject.put("ay", "${lastDevicePosition.accY}")
                jsonObject.put("az", "${lastDevicePosition.accZ}")
                jsonObject.put("gx", "${lastDevicePosition.gyrX}")
                jsonObject.put("gy", "${lastDevicePosition.gyrY}")
                jsonObject.put("gz", "${lastDevicePosition.gyrZ}")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonObject.toString().toRequestBody(mediaType)
            val request: Request = Request.Builder()
                .url(ip + "/new_data")
                .post(body)
                .build()
            try {
                val client = OkHttpClient()
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
}