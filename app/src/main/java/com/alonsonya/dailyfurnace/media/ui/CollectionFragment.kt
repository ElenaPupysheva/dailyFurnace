package com.alonsonya.dailyfurnace.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.databinding.FragmentCollectionBinding
import com.alonsonya.dailyfurnace.media.presentation.CollectionUiState
import com.alonsonya.dailyfurnace.media.presentation.CollectionViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModel()

    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearch()
        setupRecycler()

        binding.retryButton.setOnClickListener {
            viewModel.retry()
        }

        observeState()
        viewModel.loadFirstPage()
    }

    private fun setupSearch() {
        binding.collectionSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText.orEmpty())
                return true
            }
        })
    }

    private fun setupRecycler() {
        adapter = CollectionAdapter { item ->
            onFurnaceClick(item)
        }

        binding.collectionRecyclerView.adapter = adapter

        val lm = binding.collectionRecyclerView.layoutManager as GridLayoutManager
        binding.collectionRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return

                val total = lm.itemCount
                val lastVisible = lm.findLastVisibleItemPosition()

                if (total > 0 && lastVisible >= total - 6) {
                    viewModel.loadNext()
                }
            }
        })
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: CollectionUiState) = with(binding) {
        adapter.submitList(state.items)

        when {
            state.loading && state.items.isEmpty() -> {
                progressBar.isVisible = true
                collectionRecyclerView.isVisible = false
                placeholderGroup.isVisible = false
            }

            state.error != null && state.items.isEmpty() -> {
                progressBar.isVisible = false
                collectionRecyclerView.isVisible = false
                placeholderGroup.isVisible = true

                when (state.error) {
                    AppError.NoInternet -> {
                        placeholderImage.setImageResource(R.drawable.error)
                        placeholderText.setText(R.string.no_internet_message)
                    }

                    AppError.NotFound -> {
                        placeholderImage.setImageResource(R.drawable.error)
                        placeholderText.setText(R.string.not_found_message)
                    }

                    AppError.Server -> {
                        placeholderImage.setImageResource(R.drawable.error)
                        placeholderText.setText(R.string.server_error_message)
                    }

                    AppError.Empty -> {
                        placeholderImage.setImageResource(R.drawable.error)
                        placeholderText.setText(R.string.empty_message)
                    }

                    AppError.Unknown -> {
                        placeholderImage.setImageResource(R.drawable.error)
                        placeholderText.setText(R.string.generic_error_message)
                    }
                }
            }

            state.items.isEmpty() -> {
                progressBar.isVisible = false
                collectionRecyclerView.isVisible = false
                placeholderGroup.isVisible = true
                placeholderImage.setImageResource(R.drawable.error)

                if (state.query.isBlank()) {
                    placeholderText.setText(R.string.empty_collection_message)
                } else {
                    placeholderText.setText(R.string.search_not_found_message)
                }
            }

            else -> {
                progressBar.isVisible = false
                collectionRecyclerView.isVisible = true
                placeholderGroup.isVisible = false
            }
        }
    }

    private fun onFurnaceClick(item: FurnaceItem) {
        findNavController().navigate(
            R.id.detailedFragment,
            bundleOf("furnace_id" to item.id)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}