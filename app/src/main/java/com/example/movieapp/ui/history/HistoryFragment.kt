package com.example.movieapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.databinding.FragmentHistoryBinding
import com.example.movieapp.domain.models.Movie
import com.example.movieapp.presentation.history.HistoryState
import com.example.movieapp.presentation.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<HistoryViewModel>()
    private var adapter: HistoryAdapter? = null

    private lateinit var placeholderMessage: TextView
    private lateinit var historyList: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter()

        placeholderMessage = binding.placeholderMessage
        historyList = binding.historyList
        progressBar = binding.progressBar

        historyList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        historyList.adapter = adapter

        viewModel.fillData()

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        historyList.adapter = null
        _binding = null
    }

    private fun render(state: HistoryState) {
        when(state) {
            is HistoryState.Loading -> showLoading()
            is HistoryState.Content -> showContent(state.movies)
            is HistoryState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        historyList.isVisible = false
        placeholderMessage.isVisible = false
        progressBar.isVisible = true
    }

    private fun showContent(movies: List<Movie>) {
        historyList.isVisible = true
        placeholderMessage.isVisible = false
        progressBar.isVisible = false

        adapter?.movies?.clear()
        adapter?.movies?.addAll(movies)
        adapter?.notifyDataSetChanged()
    }

    private fun showEmpty(message: String) {
        historyList.isVisible = false
        placeholderMessage.isVisible = true
        progressBar.isVisible = false

        placeholderMessage.text = message
    }
}