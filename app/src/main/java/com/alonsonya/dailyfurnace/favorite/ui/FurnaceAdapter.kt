package com.alonsonya.dailyfurnace.favorite.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemFurnaceBinding
import com.alonsonya.dailyfurnace.favorite.presentation.FavoriteViewHolder

private object FurnaceDiff : DiffUtil.ItemCallback<Furnace>() {
    override fun areItemsTheSame(a: Furnace, b: Furnace) = a.furnaceId == b.furnaceId
    override fun areContentsTheSame(a: Furnace, b: Furnace) = a == b
}

class FurnaceAdapter(
    private val onItemClick: (Furnace) -> Unit
) : ListAdapter<Furnace, FavoriteViewHolder>(FurnaceDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFurnaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }
}