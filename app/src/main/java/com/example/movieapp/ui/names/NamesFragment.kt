package com.example.movieapp.ui.names

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.FragmentNamesBinding
import com.example.movieapp.domain.models.Person
import com.example.movieapp.presentation.names.NamesState
import com.example.movieapp.presentation.names.NamesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NamesFragment : Fragment() {

    private var _binding: FragmentNamesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<NamesViewModel>()

    private var textWatcher: TextWatcher? = null

    private val adapter = PersonsAdapter()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.personList.adapter = adapter

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

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.placeholderMessage.isVisible = false
        binding.personList.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = true
        binding.personList.isVisible = false

        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(persons: List<Person>) {
        binding.progressBar.isVisible = false
        binding.placeholderMessage.isVisible = false
        binding.personList.isVisible = true

        adapter.persons.clear()
        adapter.persons.addAll(persons)
        adapter.notifyDataSetChanged()
    }

    private fun render(state: NamesState) {
        when (state) {
            is NamesState.Loading -> showLoading()
            is NamesState.Error -> showError(state.message)
            is NamesState.Empty -> showEmpty(state.message)
            is NamesState.Content -> showContent(state.persons)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}