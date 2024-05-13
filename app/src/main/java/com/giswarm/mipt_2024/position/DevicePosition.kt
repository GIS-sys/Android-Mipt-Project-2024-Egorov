package com.giswarm.mipt_2024.position

data class DevicePosition(
    var accX: Double,
    var accY: Double,
    var accZ: Double,
    var gyrX: Double,
    var gyrY: Double,
    var gyrZ: Double,
    var deg: Double
)