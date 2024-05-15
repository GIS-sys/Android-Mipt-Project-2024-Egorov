package com.giswarm.mipt_2024.dataviewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BitmapModel(default: Bitmap): ViewModel() {
    val image: MutableState<Bitmap> = mutableStateOf(default)
}