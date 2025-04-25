package com.example.movieapp.data.dto

class MoviesSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDTO>
) : Response()
