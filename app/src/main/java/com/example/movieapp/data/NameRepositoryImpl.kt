package com.example.movieapp.data

import com.example.movieapp.data.dto.NameSearchResponse
import com.example.movieapp.data.dto.NamesSearchRequest
import com.example.movieapp.domain.api.NameRepository
import com.example.movieapp.domain.models.Person
import com.example.movieapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NameRepositoryImpl(private val networkClient: NetworkClient) : NameRepository {

    override fun searchNames(expression: String): Flow<Resource<List<Person>>> = flow {
        val response = networkClient.doRequest(NamesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as NameSearchResponse) {
                    val data = results.map {
                        Person(
                            id = it.id,
                            name = it.title,
                            description = it.description,
                            photoUrl = it.image
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}