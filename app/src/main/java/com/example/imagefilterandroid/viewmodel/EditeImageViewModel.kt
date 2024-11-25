package com.example.imagefilterandroid.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.data.ImageFilterDataState
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
        imagePreviewDataState.postValue(dataState)
    }

    private val imageFilterDataState = MutableLiveData<ImageFilterDataState>()
    val imageFilterUiState: LiveData<ImageFilterDataState> get() = imageFilterDataState

    fun loadImageFilters(orginalImage: Bitmap){
        Coroutines.io {
            runCatching {
                editImageFiltersUiState(isLoading = true)
                editeImageRepository.getImageFilters(getPreviewImage(orginalImage))
            }.onSuccess { imageFilters ->
                editImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure {
                editImageFiltersUiState(error = it.message.toString())
            }
        }
    }


    private fun getPreviewImage(orginalImage: Bitmap): Bitmap{
        return runCatching {
            val previewWidth = 150
            val previewHeight = orginalImage.height * previewWidth/orginalImage.width
            Bitmap.createScaledBitmap(orginalImage,previewWidth,previewHeight,false)
        }.getOrDefault(orginalImage)
    }


    private fun editImageFiltersUiState(
        isLoading:Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error: String? = null
    ){
        val dataState = ImageFilterDataState(isLoading, imageFilters,error)
        imageFilterDataState.postValue(dataState)
    }

    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState


    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io{
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editeImageRepository.saveFilteredImage(filteredBitmap)
            }.onSuccess {  saveImageUri ->
                emitSaveFilteredImageUiState(uri = saveImageUri)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading,uri,error)
        saveFilteredImageDataState.postValue(dataState)

    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )


}