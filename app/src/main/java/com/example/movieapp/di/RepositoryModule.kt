package com.example.movieapp.di

import com.example.movieapp.data.MoviesRepositoryImpl
import com.example.movieapp.domain.api.MoviesRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get())
    }
}
