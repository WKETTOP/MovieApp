package com.example.movieapp.util

import android.content.Context
import com.example.movieapp.data.MoviesRepositoryImpl
import com.example.movieapp.data.dto.LocalStorage
import com.example.movieapp.data.network.RetrofitNetworkClient
import com.example.movieapp.domain.api.MoviesInteractor
import com.example.movieapp.domain.api.MoviesRepository
import com.example.movieapp.domain.impl.MoviesInteractorImpl
import com.example.movieapp.presentation.details.DetailsPresenter
import com.example.movieapp.presentation.details.DetailsView

object Creator {
//    private fun getMoviesRepository(context: Context): MoviesRepository {
//        return MoviesRepositoryImpl(
//            RetrofitNetworkClient(context),
//            LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE))
//        )
//    }
//
//    fun provideMoviesInteractor(context: Context): MoviesInteractor {
//        return MoviesInteractorImpl(getMoviesRepository(context))
//    }

}