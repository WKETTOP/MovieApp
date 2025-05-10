package com.example.movieapp.data

import com.example.movieapp.data.dto.NameSearchResponse
import com.example.movieapp.data.dto.NamesSearchRequest
import com.example.movieapp.domain.api.NameRepository
import com.example.movieapp.domain.models.Person
import com.example.movieapp.util.Resource

class NameRepositoryImpl(private val networkClient: NetworkClient) : NameRepository {

    override fun searchNames(expression: String): Resource<List<Person>> {
        val response = networkClient.doRequest(NamesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                with(response as NameSearchResponse) {
                    Resource.Success(results.map {
                        Person(
                            id = it.id,
                            name = it.title,
                            description = it.description,
                            photoUrl = it.image
                        )
                    })
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}