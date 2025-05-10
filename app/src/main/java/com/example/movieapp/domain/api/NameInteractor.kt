package com.example.movieapp.domain.api

import com.example.movieapp.domain.models.Person

interface NameInteractor {

    fun searchNames(expression: String, consumer: NamesConsumer)

    interface NamesConsumer {
        fun consume(foundNames: List<Person>?, errorMessage: String?)
    }
}