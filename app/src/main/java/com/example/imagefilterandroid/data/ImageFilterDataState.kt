package com.example.imagefilterandroid.data

data class ImageFilterDataState(
    val isLoading:Boolean,
    val imageFilters: List<ImageFilter>? = null,
    val error: String?
)
