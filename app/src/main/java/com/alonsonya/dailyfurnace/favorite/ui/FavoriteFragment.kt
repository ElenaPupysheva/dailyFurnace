package com.alonsonya.dailyfurnace.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.FragmentFavoriteBinding
import com.alonsonya.dailyfurnace.favorite.presentation.FavoriteUiItem
import com.alonsonya.dailyfurnace.favorite.presentation.FavoriteUiState
import com.alonsonya.dailyfurnace.favorite.presentation.FavoritesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()

    private lateinit var adapter: FurnaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        binding.clearFavButton.setOnClickListener {
            viewModel.clearAllFavorites()
        }

        observeState()
    }

    private fun setupRecycler() {
        adapter = FurnaceAdapter(
            onClick = { item ->
                openDetails(item)
            },
            onHeartClick = { item ->
                viewModel.removeFromFavorites(item.id)
            }
        )

        binding.favoritesRecyclerView.adapter = adapter
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

    private fun render(state: FavoriteUiState) = with(binding) {
        adapter.submitList(state.items)

        when {
            state.loading && state.items.isEmpty() -> {
                progressBar.isVisible = true
                favoritesRecyclerView.isVisible = false
                placeholderGroup.isVisible = false
                clearFavButton.isVisible = false
            }

            state.error != null && state.items.isEmpty() -> {
                progressBar.isVisible = false
                favoritesRecyclerView.isVisible = false
                placeholderGroup.isVisible = true
                clearFavButton.isVisible = false

                placeholderImage.setImageResource(R.drawable.error)

                when (state.error) {
                    AppError.NoInternet -> {
                        placeholderText.setText(R.string.no_internet_message)
                    }

                    AppError.NotFound -> {
                        placeholderText.setText(R.string.not_found_message)
                    }

                    AppError.Server -> {
                        placeholderText.setText(R.string.server_error_message)
                    }

                    AppError.Empty -> {
                        placeholderText.setText(R.string.empty_message)
                    }

                    AppError.Unknown -> {
                        placeholderText.setText(R.string.generic_error_message)
                    }
                }
            }

            state.items.isEmpty() -> {
                progressBar.isVisible = false
                favoritesRecyclerView.isVisible = false
                placeholderGroup.isVisible = true
                clearFavButton.isVisible = false

                placeholderImage.setImageResource(R.drawable.noconnection)
                placeholderText.setText(R.string.empty_favorites_message)
            }

            else -> {
                progressBar.isVisible = false
                favoritesRecyclerView.isVisible = true
                placeholderGroup.isVisible = false
                clearFavButton.isVisible = true
            }
        }
    }

    private fun openDetails(item: FavoriteUiItem) {
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