package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
    fun getMovieDetails(movieId: String): Resource<MovieDetails>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
    fun getMovieCast(movieId: String): Resource<MovieCast>
}
