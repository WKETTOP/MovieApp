package com.example.movieapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentAboutBinding
import com.example.movieapp.domain.models.MovieDetails
import com.example.movieapp.presentation.details.AboutViewModel
import com.example.movieapp.ui.cast.MoviesCastFragment
import com.example.movieapp.presentation.details.AboutState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AboutFragment : Fragment() {

    companion object {
        private const val MOVIE_ID = "movie_id"

        fun newInstance(movieId: String) = AboutFragment().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, movieId)
            }
        }
    }

//    private val router: Router by inject()

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val aboutViewModel: AboutViewModel by viewModel {
        parametersOf(requireArguments().getString(MOVIE_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when(it) {
                is AboutState.Content -> showDetails(it.movie)
                is AboutState.Error -> showErrorMessage(it.message)
            }
        }

        binding.showCastButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailsFragment_to_moviesCastFragment,
                MoviesCastFragment.createArgs(requireArguments().getString(MOVIE_ID).orEmpty())
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDetails(movieDetails: MovieDetails) {
        binding.apply {
            details.isVisible = true
            errorMessage.isVisible = false
            title.text = movieDetails.title
            ratingValue.text = movieDetails.imDbRating
            yearValue.text = movieDetails.year
            countryValue.text = movieDetails.countries
            genreValue.text = movieDetails.genre
            directorValue.text = movieDetails.directors
            writerValue.text = movieDetails.writers
            castValue.text = movieDetails.stars
            plot.text = movieDetails.plot
        }
    }

    private fun showErrorMessage(message: String) {
        binding.apply {
            details.isVisible = false
            errorMessage.isVisible = true
            errorMessage.text = message
        }
    }
}
