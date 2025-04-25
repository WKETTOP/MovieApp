package com.example.movieapp.data

import com.example.movieapp.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}