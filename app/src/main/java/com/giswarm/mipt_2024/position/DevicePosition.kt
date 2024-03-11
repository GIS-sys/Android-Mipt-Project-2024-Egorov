package com.giswarm.mipt_2024.position

class DevicePosition {
    companion object {
        private var lastPosition: DevicePosition = DevicePosition()
        fun get(): DevicePosition {
            return lastPosition
        }
    }
}