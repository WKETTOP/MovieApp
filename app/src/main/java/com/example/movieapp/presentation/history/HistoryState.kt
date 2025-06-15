package com.example.movieapp.presentation.history

import com.example.movieapp.domain.models.Movie

sealed interface HistoryState {

    data object Loading : HistoryState

    data class Content(val movies: List<Movie>) : HistoryState

    data class Empty(val message: String) : HistoryState
}