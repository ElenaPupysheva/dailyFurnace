package com.alonsonya.dailyfurnace.furnace.ui

import android.os.Bundle
import android.util.Log
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
import coil.load
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceUiState
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FurnaceFragment : Fragment() {

    private var _binding: FragmentFurnaceBinding? = null
    private val binding get() = _binding!!

    private val vm: FurnaceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFurnaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argId = arguments?.getInt("furnace_id") ?: -1
        Log.d("FurnaceScreen", "arg furnace_id=$argId")

        binding.addFavorite.setOnClickListener {
            vm.toggleFavorite()
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

        vm.loadStartup(argId)
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
                furnaceInfo.text = furnace.shortDescription

                furnaceImage.load(furnace.imageUrl ?: furnace.thumbnailUrl) {
                    placeholder(R.drawable.fire)
                    error(R.drawable.error)
                    crossfade(true)
                }

                furnaceCard.setOnClickListener {
                    findNavController().navigate(
                        R.id.detailedFragment,
                        bundleOf("furnace_id" to furnace.id)
                    )
                }

                addFavorite.isSelected = state.isFavorite
                favoriteButton.isSelected = state.isFavorite
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