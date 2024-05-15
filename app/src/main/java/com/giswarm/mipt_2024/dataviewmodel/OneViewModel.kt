package com.giswarm.mipt_2024.dataviewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OneViewModel<T>(default: T): ViewModel() {
    val text: MutableState<T> = mutableStateOf(default)
}