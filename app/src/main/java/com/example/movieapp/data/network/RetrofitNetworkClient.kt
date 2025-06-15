package com.example.movieapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.movieapp.data.NetworkClient
import com.example.movieapp.data.dto.MovieDetailsRequest
import com.example.movieapp.data.dto.MovieCastRequest
import com.example.movieapp.data.dto.MovieSearchRequest
import com.example.movieapp.data.dto.NamesSearchRequest
import com.example.movieapp.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val imdbService: IMDbApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if ((dto !is MovieSearchRequest) && (dto !is MovieDetailsRequest) && (dto !is MovieCastRequest) && (dto !is NamesSearchRequest)) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is NamesSearchRequest -> imdbService.searchNames(dto.expression)
                    is MovieSearchRequest -> imdbService.searchMovies(dto.expression)
                    is MovieDetailsRequest -> imdbService.getMovieDetails(dto.movieId)
                    else -> imdbService.getFullCast((dto as MovieCastRequest).movieId)
                }

                response.apply { resultCode = 200 }

            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }

//        val response = when (dto) {
//            is NamesSearchRequest -> imdbService.searchNames(dto.expression).execute()
//            is MovieSearchRequest -> imdbService.searchMovies(dto.expression).execute()
//            is MovieDetailsRequest -> imdbService.getMovieDetails(dto.movieId).execute()
//            else -> imdbService.getFullCast((dto as MovieCastRequest).movieId).execute()
//    }
//        val body = response.body()
//        return if (body != null) {
//            body.apply { resultCode = response.code() }
//        } else {
//            Response().apply { resultCode = response.code() }
//        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}