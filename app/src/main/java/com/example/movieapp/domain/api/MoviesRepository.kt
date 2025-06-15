package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun searchMovies(expression: String): Flow<Resource<List<Movie>>>
    fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>>
    fun getMovieCast(movieId: String): Flow<Resource<MovieCast>>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}
