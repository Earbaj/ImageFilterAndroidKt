package com.example.imagefilterandroid.ui


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.imagefilterandroid.MainActivity
import com.example.imagefilterandroid.adapter.ImageFilterAdapter
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.databinding.ActivityEditeImageBinding
import com.example.imagefilterandroid.listeners.ImageFilterListener
import com.example.imagefilterandroid.ui.filteredImage.FilteredImageActivity
import com.example.imagefilterandroid.utils.displayToast
import com.example.imagefilterandroid.utils.show
import com.example.imagefilterandroid.viewmodel.EditeImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditeImageActivity : AppCompatActivity(),ImageFilterListener {

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }


    private lateinit var binding: ActivityEditeImageBinding
    private val viewModel: EditeImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    private lateinit var orginaImageBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(
            this, {
                val datastate = it ?: return@observe
                binding.previewProgressBar.visibility =
                    if (datastate.isLoading) View.VISIBLE else View.GONE
                datastate.bitmap?.let { bitmap ->

                    orginaImageBitmap = bitmap
                    filteredBitmap.value = bitmap

                    with(orginaImageBitmap){
                        gpuImage.setImage(this)
//                        binding.imagePreview.setImageBitmap(bitmap)
                        binding.imagePreview.show()
                        viewModel.loadImageFilters(bitmap)
                    }

                } ?: kotlin.run {
                    datastate.error?.let { error ->
                        displayToast(error)
                    }
                }
            }
        )
        viewModel.imageFilterUiState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility = if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFilterAdapter(imageFilters,this).also { adapter ->
                    binding.filterRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        filteredBitmap.observe(this, {bitmap->
            binding.imagePreview.setImageBitmap(bitmap)
        })
        viewModel.saveFilteredImageUiState.observe(this, {
            val saveFilteredImageDataState = it ?: return@observe
            if(saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            }else{
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { saveImageUri ->
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, saveImageUri)
                    startActivity(filteredImageIntent)
                }
            } ?: kotlin.run {
                Log.e("SaveImageError", "Error: ${saveFilteredImageDataState.error}")
                displayToast(saveFilteredImageDataState.error ?: "Unknown error occurred")
            }

        })
    }

    private fun prepareImagePreview(){
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                Log.d("Image bitmap","${bitmap.toString()}")
                viewModel.saveFilteredImage(applicationContext,bitmap)
            }
        }

        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(orginaImageBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }

}