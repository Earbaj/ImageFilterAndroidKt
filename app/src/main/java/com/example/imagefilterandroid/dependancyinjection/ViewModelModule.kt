package com.example.imagefilterandroid.dependancyinjection

import com.example.imagefilterandroid.viewmodel.EditeImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{
        EditeImageViewModel(editeImageRepository = get())
    }
}