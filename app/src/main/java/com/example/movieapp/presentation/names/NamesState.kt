package com.example.movieapp.presentation.names

import com.example.movieapp.domain.models.Person

sealed interface NamesState {

    data object Loading: NamesState

    data class Content(val persons: List<Person>) : NamesState

    data class Error(val message: String) : NamesState

    data class Empty(val message: String) : NamesState
}