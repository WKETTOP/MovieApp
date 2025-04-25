package com.example.movieapp.util

import android.app.Application
import com.example.movieapp.di.dataModule
import com.example.movieapp.di.interactorModule
import com.example.movieapp.di.repositoryModule
import com.example.movieapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }
}
