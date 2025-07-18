package com.alonsonya.dailyfurnace.furnace.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding

class FurnaceFragment : Fragment() {
    private var _binding: FragmentFurnaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FurnaceFragment", "onCreateView: Fragment Furnace создан!")
        _binding = FragmentFurnaceBinding.inflate(inflater, container, false)
        return binding.root
    }
}