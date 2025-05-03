package com.example.movieapp.presentation.cast

import com.example.movieapp.core.ui.RVItem
import com.example.movieapp.domain.models.MovieCastPerson

sealed interface MoviesCastRVItem : RVItem {

    data class HeaderItem(val headerText: String) : MoviesCastRVItem
    data class PersonItem(val data: MovieCastPerson) : MoviesCastRVItem
}