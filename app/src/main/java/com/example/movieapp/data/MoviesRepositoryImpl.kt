package com.example.movieapp.data

import com.example.movieapp.data.converters.MovieCastConverter
import com.example.movieapp.data.dto.LocalStorage
import com.example.movieapp.data.dto.MovieCastRequest
import com.example.movieapp.data.dto.MovieCastResponse
import com.example.movieapp.data.dto.MovieDetailsRequest
import com.example.movieapp.data.dto.MovieDetailsResponse
import com.example.movieapp.data.dto.MovieSearchRequest
import com.example.movieapp.data.dto.MovieSearchResponse
import com.example.movieapp.domain.api.MoviesRepository
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.util.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val movieCastConverter: MovieCastConverter
) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MovieSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()

                Resource.Success((response as MovieSearchResponse).results.map {
                    Movie(
                        id = it.id,
                        resultType = it.resultType,
                        image = it.image,
                        title = it.title,
                        description = it.description,
                        inFavorite = stored.contains(it.id)
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieDetails(movieId: String): Resource<MovieDetails> {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    Resource.Success(
                        MovieDetails(
                            id = id ?: "Отсутствует ID фильма",
                            title = title ?: "Название неизвестно",
                            imDbRating = imDbRating ?: "N/A",
                            year = year ?: "Год неизвестен",
                            countries = countries ?: "Страна неизвестна",
                            genre = genre ?: "Жанр неизвестен",
                            directors = directors ?: "Режиссёр неизвестен",
                            writers = writers ?: "Сценарист неизвестен",
                            stars = stars ?: "Актеры неизвестны",
                            plot = plot ?: "Описание отсутствует"
                        )
                    )
                }
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun getMovieCast(movieId: String): Resource<MovieCast> {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success(
                    data = movieCastConverter.convert(response as MovieCastResponse)
                )
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorite(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }
}
