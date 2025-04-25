package com.example.movieapp.presentation.details

class DetailsPresenter(
    private val view: DetailsView,
    private val imageUrl: String
    ) {

    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }

}