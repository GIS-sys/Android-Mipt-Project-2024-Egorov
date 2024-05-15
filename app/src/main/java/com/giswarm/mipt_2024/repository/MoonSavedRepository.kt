package com.giswarm.mipt_2024.repository

import com.giswarm.mipt_2024.position.DevicePosition

class MoonSavedRepository {
    companion object {
        @Volatile
        private var position: DevicePosition = DevicePosition()
    }

    fun setPosition(newPosition: DevicePosition) {
        // latestNewsMutex.withLock
        position = newPosition
    }
    fun getPosition(): DevicePosition = position
}