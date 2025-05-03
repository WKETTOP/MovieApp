package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Movie
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.domain.models.MovieDetails

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)
    fun getMoviesDetails(movieId: String, consumer: MovieDetailsConsumer)
    fun getMovieCast(movieId: String, consumer:MovieCastConsumer)

    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>?, errorMessage: String?)
    }

    interface MovieDetailsConsumer {
        fun consume(movieDetails: MovieDetails?, errorMessage: String?)
    }

    interface MovieCastConsumer {
        fun consume(movieCast: MovieCast?, errorMessage: String?)
    }

    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
}
