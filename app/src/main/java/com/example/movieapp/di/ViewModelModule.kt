package com.example.movieapp.di

import com.example.movieapp.presentation.details.AboutViewModel
import com.example.movieapp.presentation.details.DetailsViewModel
import com.example.movieapp.presentation.movies.MovieSearchViewModel
import com.example.movieapp.presentation.cast.MovieCastViewModel
import com.example.movieapp.presentation.history.HistoryViewModel
import com.example.movieapp.presentation.names.NamesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MovieSearchViewModel(androidApplication(),get())
    }

    viewModel { (movieId: String) ->
        AboutViewModel(movieId, get())
    }

    viewModel { (posterUrl: String) ->
        DetailsViewModel(posterUrl)
    }

    viewModel { (movieId: String) ->
        MovieCastViewModel(movieId, get())
    }

    viewModel {
        NamesViewModel(androidContext(), get())
    }

    viewModel {
        HistoryViewModel(androidContext(), get())
    }
}
