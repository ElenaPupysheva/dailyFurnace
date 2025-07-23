package com.alonsonya.dailyfurnace.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val mockFurnaces = listOf(
        Furnace(1, "Классический камин", "Традиционный дизайн", null, true),
        Furnace(2, "Русская печь", "Для настоящих ценителей", null, false),
        Furnace(3, "Современный портал", "Хай-тек стиль", null, true)
    )
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

        setupRecyclerViewWithMockData()
    }

    private fun setupRecyclerViewWithMockData() {
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = FurnaceAdapter(mockFurnaces) { _ ->
            findNavController().navigate(com.alonsonya.dailyfurnace.R.id.detailedFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}