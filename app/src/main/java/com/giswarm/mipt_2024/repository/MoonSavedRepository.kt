package com.giswarm.mipt_2024.repository

import com.giswarm.mipt_2024.position.DevicePosition
import com.giswarm.mipt_2024.recycler.RecyclerItemInputDelete

class MoonSavedRepository {
    companion object {
        @Volatile
        private var items: List<RecyclerItemInputDelete> = listOf()
    }

    fun setSavedList(newItems: List<RecyclerItemInputDelete>) {
        // latestNewsMutex.withLock
        items = newItems
    }

    fun getSavedList(): List<RecyclerItemInputDelete> = items
}