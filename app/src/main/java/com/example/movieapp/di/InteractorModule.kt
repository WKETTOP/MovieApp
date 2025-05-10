package com.example.movieapp.di

import com.example.movieapp.domain.api.MoviesInteractor
import com.example.movieapp.domain.api.NameInteractor
import com.example.movieapp.domain.impl.MoviesInteractorImpl
import com.example.movieapp.domain.impl.NameInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<MoviesInteractor> {
        MoviesInteractorImpl(get())
    }

    single<NameInteractor> {
        NameInteractorImpl(get())
    }
}
