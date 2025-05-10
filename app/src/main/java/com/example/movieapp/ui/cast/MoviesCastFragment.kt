package com.example.movieapp.ui.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentMoviesCastBinding
import com.example.movieapp.presentation.cast.MovieCastViewModel
import com.example.movieapp.presentation.cast.MovieCastState
import com.example.movieapp.ui.movieCastHeaderDelegate
import com.example.movieapp.ui.movieCastPersonDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoviesCastFragment : Fragment() {

    companion object {

        private const val ARGS_MOVIE_ID = "movie_id"

        fun createArgs(movieId: String): Bundle = bundleOf(ARGS_MOVIE_ID to movieId)

//        const val TAG = "MoviesCastFragment"

//        fun newInstance(movieId: String): Fragment {
//            return MoviesCastFragment().apply {
//                arguments = bundleOf(
//                    ARGS_MOVIE_ID to movieId
//                )
//            }
//        }
    }

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(requireArguments().getString(ARGS_MOVIE_ID))
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )

    private var _binding: FragmentMoviesCastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        moviesCastViewModel.observeState().observe(viewLifecycleOwner) {
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