package com.example.imagefilterandroid.dependancyinjection

import com.example.imagefilterandroid.repository.EditeImageRepository
import com.example.imagefilterandroid.repository.EditeImageRepositoryImplementation
import org.koin.dsl.module



val repositoryModule = module{
    factory<EditeImageRepository> {
        EditeImageRepositoryImplementation(androidContext)
    }
}