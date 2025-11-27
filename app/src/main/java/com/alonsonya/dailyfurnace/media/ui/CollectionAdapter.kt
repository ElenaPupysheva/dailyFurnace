package com.alonsonya.dailyfurnace.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.databinding.ItemFurnaceBinding
import com.alonsonya.dailyfurnace.media.presentation.CollectionViewHolder


class CollectionAdapter(
    private val furnaces: List<Furnace>,
    private val onItemClick: (Long) -> Unit
) : RecyclerView.Adapter<CollectionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionViewHolder {
        val binding = ItemFurnaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CollectionViewHolder,
        position: Int
    ) {
        val furnace = furnaces[position]
        holder.bind(furnace)
        holder.itemView.setOnClickListener {
            onItemClick(furnace.furnaceId)
        }
    }

    override fun getItemCount(): Int = furnaces.size
}
