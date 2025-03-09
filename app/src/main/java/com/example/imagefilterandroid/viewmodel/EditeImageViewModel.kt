package com.example.imagefilterandroid.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.data.ImageFilterDataState
import com.example.imagefilterandroid.data.ImagePreviewDataState
import com.example.imagefilterandroid.repository.EditeImageRepository
import com.example.imagefilterandroid.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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


    fun saveFilteredImage(context: Context, filteredBitmap: Bitmap): Uri? {
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "filtered_image.png")
            val outputStream = FileOutputStream(file)
            filteredBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
            Log.d("SaveImageRepository", "Image saved at: ${file.absolutePath}")
            return Uri.fromFile(file)  // Return the URI of the saved file
        } catch (e: Exception) {
            Log.e("SaveImageRepository", "Error saving image: ${e.message}", e)
            return null
        }
    }



//    fun saveFilteredImage(filteredBitmap: Bitmap) {
//        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                Log.d("SaveImageDebug", "Starting image save process...")
//                emitSaveFilteredImageUiState(isLoading = true)
//                val saveImageUri = editeImageRepository.saveFilteredImage(filteredBitmap) // Call the suspend function
//                Log.d("SaveImageDebug", "Received URI from Repository: $saveImageUri")
//                saveImageUri
//            }.onSuccess { saveImageUri ->
//                if (saveImageUri != null) {
//                    Log.d("SaveImageDebug", "Image successfully saved: $saveImageUri")
//                    emitSaveFilteredImageUiState(uri = saveImageUri)
//                } else {
//                    Log.e("SaveImageDebug", "Failed to save image: URI is null")
//                    emitSaveFilteredImageUiState(error = "Failed to save image: URI is null")
//                }
//            }.onFailure { exception ->
//                Log.e("SaveImageDebug", "Error saving image: ${exception.message}", exception)
//                emitSaveFilteredImageUiState(error = exception.message ?: "An unknown error occurred")
//            }
//        }
//    }





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