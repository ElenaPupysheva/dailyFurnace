package com.alonsonya.dailyfurnace.favorite.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemFurnaceBinding
import com.alonsonya.dailyfurnace.favorite.presentation.FavoriteViewHolder

class FurnaceAdapter(
    private val items: List<Furnace>,
    private val onItemClick: (Furnace) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFurnaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val furnace = items[position]
        holder.bind(furnace)
        holder.itemView.setOnClickListener { onItemClick(furnace) }
    }

    override fun getItemCount(): Int = items.size
}