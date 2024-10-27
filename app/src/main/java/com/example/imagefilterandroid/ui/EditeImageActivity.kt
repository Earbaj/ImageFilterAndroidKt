package com.example.imagefilterandroid.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.imagefilterandroid.MainActivity
import com.example.imagefilterandroid.R
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
                } ?: kotlin.run {
                    datastate.error?.let { error ->
                        displayToast(error)
                    }
                }
            }
        )
    }

    private fun prepareImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

//    private fun displayImagePreview() {
//        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
//            val inputStream = contentResolver.openInputStream(imageUri)
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            binding.imagePreview.setImageBitmap(bitmap)
//            binding.imagePreview.visibility = View.VISIBLE
//        }
//    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

}