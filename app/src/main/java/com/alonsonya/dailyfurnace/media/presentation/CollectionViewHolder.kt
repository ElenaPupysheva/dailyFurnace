package com.alonsonya.dailyfurnace.media.presentation

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.databinding.ItemCollectionBinding

class CollectionViewHolder(
    private val binding: ItemCollectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(furnace: FurnaceItem) {
        binding.furnaceName.text = furnace.title

        binding.furnaceImage.load(furnace.imageUrl ?: furnace.thumbnailUrl) {
            placeholder(R.drawable.fire)
            error(R.drawable.error)
            crossfade(true)
        }
    }
}