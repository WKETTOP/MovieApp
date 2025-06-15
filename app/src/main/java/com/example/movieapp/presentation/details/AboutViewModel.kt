package com.example.movieapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.api.MoviesInteractor
import com.example.movieapp.domain.models.MovieDetails
import kotlinx.coroutines.launch

class AboutViewModel(
    movieId: String,
    moviesInteractor: MoviesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<AboutState>()
    fun observeState(): LiveData<AboutState> = stateLiveData

    init {

        viewModelScope.launch {
            moviesInteractor
                .getMoviesDetails(movieId)
                .collect { pair ->
                    if (pair.first != null) {
                        stateLiveData.postValue(AboutState.Content(pair.first!!))
                    } else {
                        stateLiveData.postValue(AboutState.Error(pair.second ?: "Unknown error"))
                    }
                }
        }
//        moviesInteractor.getMoviesDetails(movieId, object
//            : MoviesInteractor.MovieDetailsConsumer {
//            override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
//                if (movieDetails != null) {
//                    stateLiveData.postValue(AboutState.Content(movieDetails))
//                } else {
//                    stateLiveData.postValue(AboutState.Error(errorMessage ?: "Unknown error"))
//                }
//            }
//        })
    }
}
