package com.alonsonya.dailyfurnace.media.presentation

import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemCollectionBinding

class CollectionViewHolder(
    private val binding: ItemCollectionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(furnace: Furnace) {
        binding.furnaceName.text = furnace.furnaceName

        val context = binding.root.context
        val resName = furnace.imageRes

        val resolvedResId = if (!resName.isNullOrEmpty()) {
            val foundId = context.resources.getIdentifier(
                resName,
                "drawable",
                context.packageName
            )
            if (foundId != 0) foundId else R.drawable.fire
        } else {
            R.drawable.fire
        }

        binding.furnaceImage.setImageResource(resolvedResId)
    }
}