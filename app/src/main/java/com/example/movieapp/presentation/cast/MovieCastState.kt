package com.example.movieapp.presentation.cast

import com.example.movieapp.core.ui.RVItem

sealed interface MovieCastState {

    data object Loading : MovieCastState

    data class Content(
        val fullTitle: String,
        val items: List<RVItem>
    ) : MovieCastState

    data class Error(val message: String) : MovieCastState
}