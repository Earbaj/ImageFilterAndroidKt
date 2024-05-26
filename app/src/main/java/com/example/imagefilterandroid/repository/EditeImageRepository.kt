package com.example.imagefilterandroid.repository

import android.graphics.Bitmap
import android.net.Uri

interface EditeImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
}