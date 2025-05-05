package com.example.movieapp.di

import com.example.movieapp.core.navigation.Router
import com.example.movieapp.core.navigation.RouterImpl
import org.koin.dsl.module

val navigatorModule = module {
    val router = RouterImpl()

    single<Router> { router }
    single { router.navigatorHolder }
}