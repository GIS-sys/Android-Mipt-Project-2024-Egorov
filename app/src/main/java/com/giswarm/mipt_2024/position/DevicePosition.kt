package com.giswarm.mipt_2024.position

data class DevicePosition(
    var accX: Double = 0.0,
    var accY: Double = 0.0,
    var accZ: Double = 0.0,
    var gyrX: Double = 0.0,
    var gyrY: Double = 0.0,
    var gyrZ: Double = 0.0,
    var degX: Double = 0.0,
    var degY: Double = 0.0,
    var degZ: Double = 0.0
)