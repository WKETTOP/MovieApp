package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Person
import com.example.movieapp.util.Resource

interface NameRepository {

    fun searchNames(expression: String): Resource<List<Person>>
}