package com.example.movieapp.data.dto

class MovieSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<MovieDTO>
) : Response()
