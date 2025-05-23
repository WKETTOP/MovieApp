package com.example.movieapp.data.dto

data class MovieDetailsResponse(
    val id: String?,
    val title: String?,
    val imDbRating: String?,
    val year: String?,
    val countries: String?,
    val genre: String?,
    val directors: String?,
    val writers: String?,
    val stars: String?,
    val plot: String?
) : Response()
