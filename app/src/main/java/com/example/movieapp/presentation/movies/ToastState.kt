package com.example.movieapp.presentation.movies

interface ToastState {
    object None: ToastState
    data class Show(val additionalMessage: String): ToastState
}