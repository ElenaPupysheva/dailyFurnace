package com.alonsonya.dailyfurnace.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.FragmentDetailedBinding
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { s ->
                    binding.favoriteButton.isSelected = s.isFavorite
                    s.furnace?.let { f ->
                        binding.furnaceTitle.text = f.title
                        binding.furnaceInfo.text = f.fullDescription
                        binding.furnaceImage.load(f.imageUrl ?: f.thumbnailUrl) {
                            placeholder(R.drawable.fire)
                            error(R.drawable.error)
                            crossfade(true)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
