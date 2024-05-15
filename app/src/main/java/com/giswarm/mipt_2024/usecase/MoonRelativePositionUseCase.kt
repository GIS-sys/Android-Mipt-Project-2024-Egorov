package com.giswarm.mipt_2024.usecase

import android.hardware.SensorManager
import android.util.Log
import com.giswarm.mipt_2024.position.DevicePositionManager
import com.giswarm.mipt_2024.position.MoonPositionManager
import com.giswarm.mipt_2024.repository.DevicePositionRepository
import com.giswarm.mipt_2024.repository.MoonPositionRepository
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MoonRelativePositionUseCase {
    private var devicePositionRepository: DevicePositionRepository = DevicePositionRepository()
    private var moonPositionRepository: MoonPositionRepository = MoonPositionRepository()

    fun calculateMoonPosition(): Pair<Double, Double> {
        // get data
        val pos = devicePositionRepository.getPosition()
        val moonPos = moonPositionRepository.getPosition()
        val x: Double
        val y: Double

        // device position
        val mAccelerometerData = FloatArray(3)
        mAccelerometerData[0] = pos.accX.toFloat()
        mAccelerometerData[1] = pos.accY.toFloat()
        mAccelerometerData[2] = pos.accZ.toFloat()
        val mMagnetometerData = FloatArray(3)
        mMagnetometerData[0] = pos.degX.toFloat()
        mMagnetometerData[1] = pos.degY.toFloat()
        mMagnetometerData[2] = pos.degZ.toFloat()
        val mMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(mMatrix, null, mAccelerometerData, mMagnetometerData)

        // moon vector
        val moonVector = FloatArray(3)
        moonVector[0] = (sin(moonPos.altitude / 180 * PI)).toFloat()
        moonVector[1] = (cos(moonPos.altitude / 180 * PI) * sin(moonPos.azimuth / 180 * PI)).toFloat()
        moonVector[2] = (cos(moonPos.altitude / 180 * PI) * cos(moonPos.azimuth / 180 * PI)).toFloat()

        // resulting vector
        val resVector = FloatArray(3)
        resVector[0] =
            mMatrix[0] * moonVector[0] + mMatrix[3] * moonVector[1] + mMatrix[6] * moonVector[2]
        resVector[1] =
            mMatrix[1] * moonVector[0] + mMatrix[4] * moonVector[1] + mMatrix[7] * moonVector[2]
        resVector[2] =
            mMatrix[2] * moonVector[0] + mMatrix[5] * moonVector[1] + mMatrix[8] * moonVector[2]
        val resAbs =
            sqrt(resVector[0] * resVector[0] + resVector[1] * resVector[1] + resVector[2] * resVector[2])
        x = -(resVector[0] / resAbs).toDouble()
        y = (resVector[1] / resAbs).toDouble()
        val z = (resVector[1] / resAbs).toDouble()

        // Log.d("DEBUG_MOON_POS", "$x $y $z")

        // return
        return Pair(x, y)
    }
}