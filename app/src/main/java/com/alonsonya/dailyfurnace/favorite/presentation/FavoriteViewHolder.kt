package com.alonsonya.dailyfurnace.favorite.presentation


import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemFurnaceBinding

class FavoriteViewHolder(
    private val binding: ItemFurnaceBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(furnace: Furnace) {
        binding.furnaceName.text = furnace.furnaceName

        val resId = if (furnace.imageRes != null) {
            binding.root.context.resources.getIdentifier(
                furnace.imageRes,
                "drawable",
                binding.root.context.packageName
            )
        } else {
            R.drawable.fireplace
        }

        binding.furnaceImage.setImageResource(resId)
    }
}