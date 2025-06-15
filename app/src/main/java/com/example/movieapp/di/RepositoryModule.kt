package com.example.movieapp.di

import com.example.movieapp.data.HistoryRepositoryImpl
import com.example.movieapp.data.MoviesRepositoryImpl
import com.example.movieapp.data.NameRepositoryImpl
import com.example.movieapp.domain.api.MoviesRepository
import com.example.movieapp.domain.api.NameRepository
import com.example.movieapp.domain.db.HistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get(), get(), get())
    }

    single<NameRepository> {
        NameRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }
}
