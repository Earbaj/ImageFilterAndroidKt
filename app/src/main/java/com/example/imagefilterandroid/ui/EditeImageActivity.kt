package com.example.imagefilterandroid.ui


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.imagefilterandroid.MainActivity
import com.example.imagefilterandroid.adapter.ImageFilterAdapter
import com.example.imagefilterandroid.databinding.ActivityEditeImageBinding
import com.example.imagefilterandroid.utils.displayToast
import com.example.imagefilterandroid.utils.show
import com.example.imagefilterandroid.viewmodel.EditeImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditeImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditeImageBinding

    private val viewModel: EditeImageViewModel by viewModel()

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
                    binding.imagePreview.setImageBitmap(bitmap)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(bitmap)
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
                ImageFilterAdapter(imageFilters).also { adapter ->
                    binding.filterRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }


    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

}