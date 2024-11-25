package com.example.imagefilterandroid.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.imagefilterandroid.data.ImageFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSobelEdgeDetectionFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditeImageRepositoryImplementation(private val context: Context): EditeImageRepository {
    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val orginalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((orginalBitmap.height * width)) / orginalBitmap.width
            return Bitmap.createScaledBitmap(orginalBitmap,width,height,false)
        } ?: return null
    }

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage = GPUImage(context)
        gpuImage.setImage(image)

        return withContext(Dispatchers.Default) {
            listOf(
                ImageFilter(
                    name = "Normal",
                    filter = GPUImageFilter(), // Default filter with no changes
                    filterPreview = image // Original image without any filter
                ),
                ImageFilter(
                    name = "Sepia",
                    filter = GPUImageSepiaToneFilter(),
                    filterPreview = applyFilter(gpuImage, GPUImageSepiaToneFilter())
                ),
                ImageFilter(
                    name = "Grayscale",
                    filter = GPUImageGrayscaleFilter(),
                    filterPreview = applyFilter(gpuImage, GPUImageGrayscaleFilter())
                ),
                ImageFilter(
                    name = "Invert",
                    filter = GPUImageColorInvertFilter(),
                    filterPreview = applyFilter(gpuImage, GPUImageColorInvertFilter())
                ),
                ImageFilter(
                    name = "Contrast",
                    filter = GPUImageContrastFilter(2.0f),
                    filterPreview = applyFilter(gpuImage, GPUImageContrastFilter(2.0f))
                ),
                ImageFilter(
                    name = "Brightness",
                    filter = GPUImageBrightnessFilter(0.2f),
                    filterPreview = applyFilter(gpuImage, GPUImageBrightnessFilter(0.2f))
                ),
                ImageFilter(
                    name = "Sobel Edge Detection",
                    filter = GPUImageSobelEdgeDetectionFilter(),
                    filterPreview = applyFilter(gpuImage, GPUImageSobelEdgeDetectionFilter())
                ),
                ImageFilter(
                    name = "Color Matrix - Warm Tone",
                    filter = GPUImageColorMatrixFilter(
                        1.0f,
                        floatArrayOf(
                            1.5f, 0.0f, 0.0f, 0.0f, 0.0f,
                            0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                            0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
                            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                        )
                    ),
                    filterPreview = applyFilter(
                        gpuImage,
                        GPUImageColorMatrixFilter(
                            1.0f,
                            floatArrayOf(
                                1.5f, 0.0f, 0.0f, 0.0f, 0.0f,
                                0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                                0.0f, 0.0f, 0.8f, 0.0f, 0.0f,
                                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                            )
                        )
                    )
                ),
                ImageFilter(
                    name = "Color Matrix - Cool Tone",
                    filter = GPUImageColorMatrixFilter(
                        1.0f,
                        floatArrayOf(
                            0.8f, 0.0f, 0.0f, 0.0f, 0.0f,
                            0.0f, 0.9f, 0.0f, 0.0f, 0.0f,
                            0.0f, 0.0f, 1.2f, 0.0f, 0.0f,
                            0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                        )
                    ),
                    filterPreview = applyFilter(
                        gpuImage,
                        GPUImageColorMatrixFilter(
                            1.0f,
                            floatArrayOf(
                                0.8f, 0.0f, 0.0f, 0.0f, 0.0f,
                                0.0f, 0.9f, 0.0f, 0.0f, 0.0f,
                                0.0f, 0.0f, 1.2f, 0.0f, 0.0f,
                                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                            )
                        )
                    )
                ),
                ImageFilter(
                    name = "Color Matrix - Grayscale",
                    filter = GPUImageColorMatrixFilter(
                        1.0f,
                        floatArrayOf(
                            0.299f, 0.587f, 0.114f, 0f, 0f,
                            0.299f, 0.587f, 0.114f, 0f, 0f,
                            0.299f, 0.587f, 0.114f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    ),
                    filterPreview = applyFilter(
                        gpuImage,
                        GPUImageColorMatrixFilter(
                            1.0f,
                            floatArrayOf(
                                0.299f, 0.587f, 0.114f, 0f, 0f,
                                0.299f, 0.587f, 0.114f, 0f, 0f,
                                0.299f, 0.587f, 0.114f, 0f, 0f,
                                0f, 0f, 0f, 1f, 0f
                            )
                        )
                    )
                )
            )
        }
    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
        return try {
            val mediaStorageDirection = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved Images"
            )
            if(!mediaStorageDirection.exists()){
                mediaStorageDirection.mkdirs()
            }
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File(mediaStorageDirection,fileName)
            saveFile(file,filteredBitmap)
            FileProvider.getUriForFile(context,"${context.packageName}.provider",file)
        }catch (exception: Exception){
            null
        }
    }

    private fun saveFile(file: File, bitmap: Bitmap){
        with(FileOutputStream(file)){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
    }


    private fun applyFilter(gpuImage: GPUImage, filter: GPUImageFilter): Bitmap {
        gpuImage.setFilter(filter)
        return gpuImage.bitmapWithFilterApplied
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }
}