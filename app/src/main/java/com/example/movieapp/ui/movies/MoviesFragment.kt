package com.example.movieapp.ui.movies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.core.navigation.Router
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.presentation.movies.MovieSearchViewModel
import com.example.movieapp.ui.details.DetailsFragment
import com.example.movieapp.ui.movies.models.MoviesState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val router: Router by inject()

    private val viewModel by viewModel<MovieSearchViewModel>()

    private var isClickAllowed = true

    private var textWatcher: TextWatcher? = null

    private val handler = Handler(Looper.getMainLooper())

    private val adapter = MoviesAdapter(
        object : MoviesAdapter.MovieClickListener {
            override fun onMovieClicked(movie: Movie) {
                if (clickDebounce()) {
                   router.openFragment(
                       DetailsFragment.newInstance(
                           movieId = movie.id,
                           posterUrl = movie.image
                       )
                   )
                }
            }

            override fun onFavoriteToggle(movie: Movie) {
                viewModel.toggleFavorite(movie)
            }
        }
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResult.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchResult.adapter = adapter

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
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }

        viewModel.observerState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observerShowToast().observe(viewLifecycleOwner) { toast ->
            if (toast != null) {
                showToast(toast)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.progressBar.isVisible = true
        binding.placeholderMessage.isVisible = false
        binding.searchResult.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.searchResult.isVisible = false

        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.searchResult.isVisible = true

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
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}