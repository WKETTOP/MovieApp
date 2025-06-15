package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Person
import kotlinx.coroutines.flow.Flow

interface NameInteractor {

    fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>>

//    fun searchNames(expression: String, consumer: NamesConsumer)

//    interface NamesConsumer {
//        fun consume(foundNames: List<Person>?, errorMessage: String?)
//    }
}