package com.alonsonya.dailyfurnace.furnace.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FurnaceFragment : Fragment() {

    private var _binding: FragmentFurnaceBinding? = null
    private val binding get() = _binding!!

    private val vm: FurnaceViewModel by viewModel()

    private var currentProductId: Int? = null

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
        val idFromArgs = arguments?.getInt("furnace_id") ?: -1
        vm.loadStartup(idFromArgs)
        val id = arguments?.getInt("furnace_id") ?: -1
        Log.d("FurnaceScreen", "arg furnace_id=$id")
        if (id != -1) vm.loadById(id)
        binding.addFavorite.setOnClickListener { vm.toggleFavorite() }
        binding.favoriteButton.setOnClickListener { vm.toggleFavorite() }

        val openDetails: (View) -> Unit = {
            currentProductId?.let { pid ->
                findNavController().navigate(
                    R.id.detailedFragment,
                    bundleOf("product_id" to pid)
                )
            }
        }
        binding.furnaceImage.setOnClickListener(openDetails)
        binding.furnaceTitle.setOnClickListener(openDetails)
        binding.furnaceInfo.setOnClickListener(openDetails)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { s ->
                    s.product?.let { p ->
                        currentProductId = p.id
                        binding.furnaceTitle.text = p.name
                        binding.furnaceInfo.text = p.description.orEmpty()
                        binding.furnaceImage.load(p.image_url) {
                            placeholder(R.drawable.fireplace)
                            error(R.drawable.fireplace)
                            crossfade(true)
                        }
                    }
                    binding.addFavorite.isSelected = s.isFavorite
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
