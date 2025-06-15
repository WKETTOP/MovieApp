package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Person
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface NameRepository {

    fun searchNames(expression: String): Flow<Resource<List<Person>>>
}