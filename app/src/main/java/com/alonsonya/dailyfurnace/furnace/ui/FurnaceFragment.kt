package com.alonsonya.dailyfurnace.furnace.ui

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
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding
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

        // 1) грузим печь по аргументу
        val id = arguments?.getInt("furnace_id") ?: -1
        if (id != -1) vm.loadById(id)

        // 2) наблюдаем стейт
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { s ->
                    // данные
                    s.product?.let { p ->
                        binding.furnaceTitle.text = p.name
                        binding.furnaceInfo.text  = p.description.orEmpty()
                        binding.furnaceImage.load(p.image_url) {
                            placeholder(R.drawable.fireplace)
                            error(R.drawable.fireplace)
                            crossfade(true)
                        }
                    }
                    // «избранное» — подсветим текстовую кнопку
                    binding.addFavorite.isSelected = s.isFavorite
                }
            }
        }

        // 3) клик «в избранное»
        binding.addFavorite.setOnClickListener { vm.toggleFavorite() }
        // если хочешь, второй дублирующий клик повесь на ImageButton:
        // binding.root.findViewById<ImageButton>(R.id.<придумай_id>).setOnClickListener { vm.toggleFavorite() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
