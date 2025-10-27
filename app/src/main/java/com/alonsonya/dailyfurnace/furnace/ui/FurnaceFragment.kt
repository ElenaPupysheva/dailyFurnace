package com.alonsonya.dailyfurnace.furnace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.mockFurnaces
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding
import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
class FurnaceFragment : Fragment() {
    private var _binding: FragmentFurnaceBinding? = null
    private val binding get() = _binding!!

    private val vm: FurnaceViewModel by viewModel()
    private var currentId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFurnaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentId = arguments?.getInt("furnace_id") ?: -1

        val f = (if (currentId != -1) mockFurnaces.find { it.furnaceId == currentId } else null)
            ?: mockFurnaces.firstOrNull()
        currentId = f?.furnaceId ?: -1
        bindFurnace(f)

        binding.furnaceImage.setOnClickListener {
            findNavController().navigate(R.id.detailedFragment)
        }

        binding.addFavorite.setOnClickListener { if (currentId != -1) vm.toggleFavorite(currentId) }

    }

    private fun bindFurnace(f: Furnace?) {
        if (f == null) return
        binding.furnaceTitle.text = f.furnaceName
        binding.furnaceInfo.text = f.furnaceInfo
        val imgName = f.imageRes
        val resId = if (!imgName.isNullOrBlank()) {
            val id = resources.getIdentifier(imgName, "drawable", requireContext().packageName)
            if (id != 0) id else R.drawable.fireplace
        } else R.drawable.fireplace
        binding.furnaceImage.setImageResource(resId)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
