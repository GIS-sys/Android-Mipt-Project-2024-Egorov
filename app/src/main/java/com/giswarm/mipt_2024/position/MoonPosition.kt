package com.giswarm.mipt_2024.position

class MoonPosition {
    companion object {
        private var lastPosition: MoonPosition = MoonPosition()
        fun get(): MoonPosition {
            return lastPosition
        }
    }

    var rightAscension: Double = 0.0
    var declination: Double = 0.0
}