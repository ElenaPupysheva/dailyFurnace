package com.alonsonya.dailyfurnace.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.FragmentDetailedBinding
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceUiState
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailedFragment : Fragment() {

    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!

    private val vm: FurnaceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("furnace_id") ?: -1
        if (id != -1) vm.loadById(id)

        binding.toolbarDetailed.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.favoriteButton.setOnClickListener {
            vm.toggleFavorite()
        }

        binding.retryButton.setOnClickListener {
            vm.retry()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: FurnaceUiState) = with(binding) {
        when {
            state.loading -> {
                progressBar.isVisible = true
                contentGroup.isVisible = false
                placeholderGroup.isVisible = false
            }

            state.furnace != null -> {
                progressBar.isVisible = false
                contentGroup.isVisible = true
                placeholderGroup.isVisible = false

                val furnace = state.furnace

                furnaceTitle.text = furnace.title
                furnaceInfo.text = furnace.fullDescription
                favoriteButton.isSelected = state.isFavorite

                furnaceImage.load(furnace.imageUrl ?: furnace.thumbnailUrl) {
                    placeholder(R.drawable.fire)
                    error(R.drawable.error)
                    crossfade(true)
                }
            }

            state.error != null -> {
                progressBar.isVisible = false
                contentGroup.isVisible = false
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

            else -> {
                progressBar.isVisible = false
                contentGroup.isVisible = false
                placeholderGroup.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}