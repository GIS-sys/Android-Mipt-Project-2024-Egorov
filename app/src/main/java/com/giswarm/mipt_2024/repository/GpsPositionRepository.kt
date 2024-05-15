package com.giswarm.mipt_2024.repository

import com.giswarm.mipt_2024.position.GpsPosition
import com.giswarm.mipt_2024.position.MoonPosition

class GpsPositionRepository {
    companion object {
        @Volatile
        private var position: GpsPosition = GpsPosition()
    }

    fun setPosition(newPosition: GpsPosition) {
        position = newPosition
    }

    fun getPosition(): GpsPosition = position
}