package com.example.imagefilterandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagefilterandroid.R
import com.example.imagefilterandroid.databinding.ActivityEditeImageBinding

class EditeImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditeImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edite_image)
    }
}