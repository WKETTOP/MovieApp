package com.example.movieapp.domain.impl

import com.example.movieapp.domain.db.HistoryInteractor
import com.example.movieapp.domain.db.HistoryRepository
import com.example.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository
) : HistoryInteractor {

    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }
}