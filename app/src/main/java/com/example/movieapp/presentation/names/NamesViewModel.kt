package com.example.movieapp.presentation.names

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.R
import com.example.movieapp.domain.api.NameInteractor
import com.example.movieapp.domain.models.Person
import com.example.movieapp.presentation.SingleLiveEvent

class NamesViewModel(private val context: Context, private val namesInteractor: NameInteractor) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val stateLiveData = MutableLiveData<NamesState>()
    fun observerState(): LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observerShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun onCleared()  {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(NamesState.Loading)

            namesInteractor.searchNames(
                newSearchText,
                object : NameInteractor.NamesConsumer {
                    override fun consume(foundNames: List<Person>?, errorMessage: String?) {
                        val persons = mutableListOf<Person>()
                        if (foundNames != null) {
                            persons.addAll(foundNames)
                        }
                        when {
                            errorMessage != null -> {
                                renderState(
                                    NamesState.Error(
                                        message = context.getString(R.string.something_went_wrong)
                                    )
                                )
                                showToast.postValue(errorMessage)
                            }

                            persons.isEmpty() -> {
                                renderState(
                                    NamesState.Empty(
                                        message = context.getString(R.string.nothing_found)
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    NamesState.Content(
                                        persons = persons
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    private fun renderState(state: NamesState) {
        stateLiveData.postValue(state)
    }
}