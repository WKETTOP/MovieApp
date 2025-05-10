package com.example.movieapp.data.dto

class NameSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<PersonDto>
) : Response()