package com.alonsonya.dailyfurnace.furnace.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alonsonya.dailyfurnace.data.EXTRA_FURNACE
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.FragmentFurnaceBinding

class FurnaceFragment : Fragment() {
    private var _binding: FragmentFurnaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentFurnace: Furnace

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FurnaceFragment", "onCreateView: Fragment Furnace создан!")
        _binding = FragmentFurnaceBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.furnaceImage.setOnClickListener {
            findNavController().navigate(com.alonsonya.dailyfurnace.R.id.detailedFragment)
        }

        //bindTrackInfo(currentFurnace)

        //val jsonTrack = intent.getStringExtra(EXTRA_FURNACE) ?: return finish()
        //currentFurnace = Gson().fromJson(jsonTrack, Furnace::class.java) ?: return finish()



        binding.addFavorite.setOnClickListener {
            //TODO

        }
    }
}