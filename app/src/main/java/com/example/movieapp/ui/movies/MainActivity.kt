package com.example.movieapp.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.presentation.movies.MovieSearchViewModel
import com.example.movieapp.ui.movies.models.MoviesState
import com.example.movieapp.ui.details.DetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<MovieSearchViewModel>()

    private var isClickAllowed = true

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    private val handler = Handler(Looper.getMainLooper())

    private val adapter = MoviesAdapter(
        object : MoviesAdapter.MovieClickListener {
            override fun onMovieClicked(movie: Movie) {
                if (clickDebounce()) {
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    intent.putExtra("poster", movie.image)
                    startActivity(intent)
                }
            }

            override fun onFavoriteToggle(movie: Movie) {
                viewModel.toggleFavorite(movie)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        viewModel = ViewModelProvider(this, MovieSearchViewModel.getMovieModelFactory())[MovieSearchViewModel::class.java]

        placeholderMessage = findViewById(R.id.placeholder_message)
        queryInput = findViewById(R.id.input_edit_text)
        moviesList = findViewById(R.id.search_result)
        progressBar = findViewById(R.id.progress_bar)

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        viewModel.observerState().observe(this) {
            render(it)
        }

        viewModel.observerShowToast().observe(this) {toast ->
            if (toast != null) {
                showToast(toast)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoading() {
        progressBar.isVisible = true
        placeholderMessage.isVisible = false
        moviesList.isVisible = false
    }

     private fun showError(errorMessage: String) {
        progressBar.isVisible = false
        placeholderMessage.isVisible = true
        moviesList.isVisible = false

        placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        progressBar.isVisible = false
        placeholderMessage.isVisible = false
        moviesList.isVisible = true

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

     private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Content -> showContent(state.movies)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
