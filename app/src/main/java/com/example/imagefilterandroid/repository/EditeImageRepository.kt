package com.example.imagefilterandroid.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.imagefilterandroid.data.ImageFilter

interface EditeImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap) : List<ImageFilter>
}