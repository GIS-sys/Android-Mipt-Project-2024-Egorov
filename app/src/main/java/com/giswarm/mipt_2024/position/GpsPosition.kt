package com.giswarm.mipt_2024.position

class GpsPosition {
    companion object {
        private var lastPosition: GpsPosition = GpsPosition()
        fun get(): GpsPosition {
            return lastPosition
        }
    }

    var lat: Double = 0.0
    var lng: Double = 0.0
}