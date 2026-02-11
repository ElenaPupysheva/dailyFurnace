package com.alonsonya.dailyfurnace.media.presentation

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemCollectionBinding

class CollectionViewHolder(
    private val binding: ItemCollectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(furnace: Furnace) {
        binding.furnaceName.text = furnace.furnaceName

        binding.furnaceImage.load(furnace.imageRes) {
            placeholder(R.drawable.fire)
            error(R.drawable.fire)
            crossfade(true)
        }
    }
}