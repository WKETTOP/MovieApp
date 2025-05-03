package com.example.movieapp.ui.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMoviesCastBinding
import com.example.movieapp.ui.cast.models.MovieCastState
import com.example.movieapp.ui.movieCastHeaderDelegate
import com.example.movieapp.ui.movieCastPersonDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoviesCastActivity : AppCompatActivity() {

    companion object {

        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(context: Context, movieId: String): Intent {
            return Intent(context, MoviesCastActivity::class.java).apply {
                putExtra(ARGS_MOVIE_ID, movieId)
            }
        }
    }

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(intent.getStringExtra(ARGS_MOVIE_ID))
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )

    private lateinit var binding: ActivityMoviesCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(this)

        moviesCastViewModel.observeState().observe(this) {
            when (it) {
                is MovieCastState.Loading -> showLoading()
                is MovieCastState.Error -> showError(it)
                is MovieCastState.Content -> showContent(it)
            }
        }
    }

    private fun showLoading() {
        binding.contentContainer.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.progressBar.isVisible = true
    }

    private fun showError(state: MovieCastState.Error) {
        binding.contentContainer.isVisible = false
        binding.progressBar.isVisible = false

        binding.errorMessageTextView.isVisible = true
        binding.errorMessageTextView.text = state.message
    }

    private fun showContent(state: MovieCastState.Content) {
        binding.progressBar.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.contentContainer.isVisible = true

        binding.movieTitle.text = state.fullTitle
        adapter.items = state.items

        adapter.notifyDataSetChanged()
    }
}
