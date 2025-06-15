package com.example.movieapp.data

import com.example.movieapp.data.converters.MovieCastConverter
import com.example.movieapp.data.converters.MovieDbConvertor
import com.example.movieapp.data.db.AppDatabase
import com.example.movieapp.data.dto.LocalStorage
import com.example.movieapp.data.dto.MovieCastRequest
import com.example.movieapp.data.dto.MovieCastResponse
import com.example.movieapp.data.dto.MovieDTO
import com.example.movieapp.data.dto.MovieDetailsRequest
import com.example.movieapp.data.dto.MovieDetailsResponse
import com.example.movieapp.data.dto.MovieSearchRequest
import com.example.movieapp.data.dto.MovieSearchResponse
import com.example.movieapp.domain.api.MoviesRepository
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val movieCastConverter: MovieCastConverter,
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor
) : MoviesRepository {

    override fun searchMovies(expression: String): Flow<Resource<List<Movie>>> = flow {
        val response = networkClient.doRequest(MovieSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                val stored = localStorage.getSavedFavorites()
                with(response as MovieSearchResponse) {
                    saveMovie(results)
                    emit(Resource.Success((response).results.map {
                        Movie(
                            id = it.id,
                            resultType = it.resultType,
                            image = it.image,
                            title = it.title,
                            description = it.description,
                            inFavorite = stored.contains(it.id)
                        )
                    }))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                with(response as MovieDetailsResponse) {
                    emit(Resource.Success(
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
                    ))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getMovieCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequest(MovieCastRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            200 -> {
                emit(Resource.Success(
                    data = movieCastConverter.convert(response as MovieCastResponse)
                ))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorite(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }

    private suspend fun saveMovie(movies: List<MovieDTO>) {
        val movieEntities = movies.map { movie -> movieDbConvertor.map(movie) }
        appDatabase.movieDao().insertMovies(movieEntities)
    }
}
