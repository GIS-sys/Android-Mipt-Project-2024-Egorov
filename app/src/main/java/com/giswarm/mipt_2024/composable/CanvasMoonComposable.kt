package com.giswarm.mipt_2024.composable

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import com.giswarm.mipt_2024.dataviewmodel.BitmapModel
import com.giswarm.mipt_2024.dataviewmodel.OneViewModel

@Composable
fun CanvasMoonComposable(bitmapModel: BitmapModel) {
    Image(
        bitmap = bitmapModel.image.value.asImageBitmap(),
        contentDescription = "some useful description",
    )
}