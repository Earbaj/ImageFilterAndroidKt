package com.example.imagefilterandroid.ui


import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.imagefilterandroid.MainActivity
import com.example.imagefilterandroid.adapter.ImageFilterAdapter
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.databinding.ActivityEditeImageBinding
import com.example.imagefilterandroid.listeners.ImageFilterListener
import com.example.imagefilterandroid.utils.displayToast
import com.example.imagefilterandroid.utils.show
import com.example.imagefilterandroid.viewmodel.EditeImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditeImageActivity : AppCompatActivity(),ImageFilterListener {

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