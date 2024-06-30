package com.example.imagefilterandroid.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagefilterandroid.data.ImagePreviewDataState
import com.example.imagefilterandroid.repository.EditeImageRepository
import com.example.imagefilterandroid.utils.Coroutines

class EditeImageViewModel(private val editeImageRepository: EditeImageRepository) : ViewModel() {
    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUiState: LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            kotlin.runCatching {
                editeImagePreviewUiState(isLoading = true)
                editeImageRepository.prepareImagePreview(imageUri)
            }.onSuccess {bitmap->
                if (bitmap != null){
                    editeImagePreviewUiState(bitmap = bitmap)
                }else{
                    editeImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }
                .onFailure {
                    editeImagePreviewUiState(error = it.message.toString())
                }
        }
    }

    private  fun editeImagePreviewUiState(
        isLoading: Boolean = false,
        bitmap: Bitmap? = null,
        error: String? = null
    ){
        val dataState = ImagePreviewDataState(isLoading,bitmap, error)
        imagePreviewDataState.value = dataState
    }
}