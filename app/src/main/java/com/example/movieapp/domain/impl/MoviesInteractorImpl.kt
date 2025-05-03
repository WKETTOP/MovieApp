package com.example.movieapp.domain.impl

import java.util.concurrent.Executors
import com.example.movieapp.domain.api.MoviesInteractor
import com.example.movieapp.domain.api.MoviesRepository
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.util.Resource

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(
        expression: String,
        consumer: MoviesInteractor.MoviesConsumer
    ) {
        executor.execute {
            when (val resource = repository.searchMovies(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun getMoviesDetails(
        movieId: String,
        consumer: MoviesInteractor.MovieDetailsConsumer
    ) {
        executor.execute {
            when (val resource = repository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(resource.data, resource.message)
                }
            }
        }
    }

    override fun getMovieCast(
        movieId: String,
        consumer: MoviesInteractor.MovieCastConsumer
    ) {
        executor.execute {
            when (val resource = repository.getMovieCast(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(resource.data, resource.message)
                }
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.removeMovieFromFavorites(movie)
    }
}
