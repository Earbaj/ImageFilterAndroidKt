package com.example.imagefilterandroid.ui.filteredImage

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.imagefilterandroid.R
import com.example.imagefilterandroid.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {
    companion object {
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding: ActivityFilteredImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_image)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the URI of the image passed in the intent
        val imageUri: Uri? = intent.getParcelableExtra(KEY_IMAGE_URI)

        imageUri?.let {
            // Display the image using ImageView
            binding.imageView.setImageURI(it)
        } ?: run {
            // Handle error if URI is null
            displayToast("Failed to load image")
        }
    }

    private fun displayToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}