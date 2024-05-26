package com.example.imagefilterandroid.ui

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.imagefilterandroid.MainActivity
import com.example.imagefilterandroid.R
import com.example.imagefilterandroid.databinding.ActivityEditeImageBinding

class EditeImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditeImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        displayImagePreview()
    }

    private fun displayImagePreview(){
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let {imageUri->
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }

    private fun setListeners(){
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

}