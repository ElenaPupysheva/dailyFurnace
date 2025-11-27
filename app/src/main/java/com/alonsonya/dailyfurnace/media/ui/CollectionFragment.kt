package com.alonsonya.dailyfurnace.media.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alonsonya.dailyfurnace.databinding.FragmentCollectionBinding
import com.alonsonya.dailyfurnace.media.presentation.CollectionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class CollectionFragment : Fragment()  {
    private val viewModel: CollectionViewModel by viewModel()
    private var _binding: FragmentCollectionBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
