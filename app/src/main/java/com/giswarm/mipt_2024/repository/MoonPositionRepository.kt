package com.giswarm.mipt_2024.repository

import com.giswarm.mipt_2024.position.MoonPosition

class MoonPositionRepository {
    companion object {
        @Volatile
        private var position: MoonPosition = MoonPosition()
    }

    fun setPosition(newPosition: MoonPosition) {
        position = newPosition
    }

    fun getPosition(): MoonPosition = position
}