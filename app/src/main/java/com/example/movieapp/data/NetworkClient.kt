package com.example.movieapp.data

import com.example.movieapp.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}