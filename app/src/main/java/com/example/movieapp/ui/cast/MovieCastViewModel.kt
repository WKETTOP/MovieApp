package com.example.movieapp.ui.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.api.MoviesInteractor
import com.example.movieapp.domain.models.MovieCast
import com.example.movieapp.presentation.cast.MoviesCastRVItem
import com.example.movieapp.ui.cast.models.MovieCastState

class MovieCastViewModel(
    movieId: String,
    moviesInteractor: MoviesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<MovieCastState>()
    fun observeState(): LiveData<MovieCastState> = stateLiveData

    init {
        stateLiveData.postValue(MovieCastState.Loading)

        moviesInteractor.getMovieCast(movieId, object : MoviesInteractor.MovieCastConsumer {
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null) {
                    stateLiveData.postValue(castToUiStateContent(movieCast))
                } else {
                    stateLiveData.postValue(MovieCastState.Error(errorMessage ?: "Unknown error"))
                }
            }

        })
    }

    private fun castToUiStateContent(cast: MovieCast): MovieCastState {
        val items = buildList<MoviesCastRVItem> {
            if (cast.directors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Directors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.writers.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Writers")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.actors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Actors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.others.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Others")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }
        }

        return MovieCastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )
    }
}