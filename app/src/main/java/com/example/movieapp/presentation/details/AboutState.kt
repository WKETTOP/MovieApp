package com.example.movieapp.presentation.details

import com.example.movieapp.domain.models.MovieDetails

sealed interface AboutState {

    data class Content(val movie: MovieDetails) : AboutState
    data class Error(val message: String) : AboutState
}
