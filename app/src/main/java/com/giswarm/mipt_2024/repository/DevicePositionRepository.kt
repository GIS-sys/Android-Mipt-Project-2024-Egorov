package com.giswarm.mipt_2024.repository

import com.giswarm.mipt_2024.position.DevicePosition

class DevicePositionRepository {
    companion object {
        @Volatile
        private var position: DevicePosition = DevicePosition()
    }

    fun setPosition(newPosition: DevicePosition) {
        position = newPosition
    }

    fun setAcc(x: Double, y: Double, z: Double) {
        position.accX = x
        position.accY = y
        position.accZ = z
    }

    fun setGyr(x: Double, y: Double, z: Double) {
        position.gyrX = x
        position.gyrY = y
        position.gyrZ = z
    }

    fun setCom(x: Double, y: Double, z: Double) {
        position.degX = x
        position.degY = y
        position.degZ = z
    }

    fun getPosition(): DevicePosition = position
}