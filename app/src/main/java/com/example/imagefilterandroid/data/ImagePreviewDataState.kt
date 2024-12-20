package com.example.imagefilterandroid.data

import android.graphics.Bitmap

data class ImagePreviewDataState(
    val isLoading: Boolean,
    val bitmap: Bitmap?,
    val error: String?
)