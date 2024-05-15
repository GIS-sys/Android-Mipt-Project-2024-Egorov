package com.giswarm.mipt_2024.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.giswarm.mipt_2024.dataviewmodel.OneViewModel

@Composable
fun <T> OneTextComposable(oneViewModel: OneViewModel<T>) {
    MaterialTheme {
        Surface {
            Text(text = "Hello! ${oneViewModel.text.value}")
        }
    }
}