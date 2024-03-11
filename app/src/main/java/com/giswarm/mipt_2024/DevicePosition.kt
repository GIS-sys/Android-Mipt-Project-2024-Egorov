package com.giswarm.mipt_2024

class DevicePosition {
    companion object {
        private var lastPosition: DevicePosition = DevicePosition()
        fun get(): DevicePosition {
            return lastPosition
        }
    }

    var lat: Double = 0.0
    var lng: Double = 0.0
}