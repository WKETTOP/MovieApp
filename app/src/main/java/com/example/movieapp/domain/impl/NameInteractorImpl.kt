package com.example.movieapp.domain.impl


import com.example.movieapp.domain.api.NameInteractor
import com.example.movieapp.domain.api.NameRepository
import com.example.movieapp.domain.models.Person
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class NameInteractorImpl(private val repository: NameRepository) : NameInteractor {

    override fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchNames(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    //    private val executor = Executors.newCachedThreadPool()
//
//    override fun searchNames(expression: String, consumer: NameInteractor.NamesConsumer) {
//        executor.execute {
//            when (val resource = repository.searchNames(expression)) {
//                is Resource.Success -> consumer.consume(resource.data, null)
//                is Resource.Error -> consumer.consume(resource.data, resource.message)
//            }
//        }
//    }

}