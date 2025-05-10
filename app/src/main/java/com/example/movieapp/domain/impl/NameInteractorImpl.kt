package com.example.movieapp.domain.impl


import com.example.movieapp.domain.api.NameInteractor
import com.example.movieapp.domain.api.NameRepository
import com.example.movieapp.util.Resource
import java.util.concurrent.Executors

class NameInteractorImpl(private val repository: NameRepository) : NameInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchNames(expression: String, consumer: NameInteractor.NamesConsumer) {
        executor.execute {
            when (val resource = repository.searchNames(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(resource.data, resource.message)
            }
        }
    }
}