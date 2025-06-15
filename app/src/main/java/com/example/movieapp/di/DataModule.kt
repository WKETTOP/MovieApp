package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.data.NetworkClient
import com.example.movieapp.data.converters.MovieCastConverter
import com.example.movieapp.data.converters.MovieDbConvertor
import com.example.movieapp.data.db.AppDatabase
import com.example.movieapp.data.dto.LocalStorage
import com.example.movieapp.data.network.IMDbApiService
import com.example.movieapp.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single { LocalStorage(get()) }

    factory { MovieCastConverter() }

    factory { MovieDbConvertor() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<IMDbApiService> {
        Retrofit.Builder()
            .baseUrl("https://tv-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IMDbApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }
}
