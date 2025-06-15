package com.example.movieapp.data

import com.example.movieapp.data.converters.MovieDbConvertor
import com.example.movieapp.data.db.AppDatabase
import com.example.movieapp.data.db.entity.MovieEntity
import com.example.movieapp.domain.db.HistoryRepository
import com.example.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor
) : HistoryRepository {

    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDatabase.movieDao().getMovies()
        emit(convertFromEntity(movies))
    }

    private fun convertFromEntity(movies: List<MovieEntity>) : List<Movie> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }
}