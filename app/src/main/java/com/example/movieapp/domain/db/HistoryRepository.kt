package com.example.movieapp.domain.db

import com.example.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun historyMovies(): Flow<List<Movie>>
}