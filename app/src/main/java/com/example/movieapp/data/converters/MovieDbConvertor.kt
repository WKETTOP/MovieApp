package com.example.movieapp.data.converters

import com.example.movieapp.data.db.entity.MovieEntity
import com.example.movieapp.data.dto.MovieDTO
import com.example.movieapp.domain.models.Movie

class MovieDbConvertor {

    fun map(movie: MovieDTO): MovieEntity {
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description, inFavorite = false)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.resultType, movie.image, movie.title, movie.description, movie.inFavorite)
    }
}