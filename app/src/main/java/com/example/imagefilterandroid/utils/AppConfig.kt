package com.example.imagefilterandroid.utils

import android.app.Application
import com.example.imagefilterandroid.dependancyinjection.repositoryModule
import com.example.imagefilterandroid.dependancyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


@Suppress("unused")
class AppConfig: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}