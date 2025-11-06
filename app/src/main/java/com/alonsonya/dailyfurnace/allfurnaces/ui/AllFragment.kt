package com.alonsonya.dailyfurnace.allfurnaces.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alonsonya.dailyfurnace.allfurnaces.ui.AllAdapter
import com.alonsonya.dailyfurnace.allfurnaces.presentation.AllViewModel
import com.alonsonya.dailyfurnace.databinding.FragmentAllBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllFragment : Fragment(){

    private val vm: AllViewModel by viewModel()
    private var _binding: FragmentAllBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AllAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }
}