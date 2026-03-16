package com.alonsonya.dailyfurnace.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.databinding.ItemCollectionBinding
import com.alonsonya.dailyfurnace.media.presentation.CollectionViewHolder

class CollectionAdapter(    private val onItemClick: (FurnaceItem) -> Unit
) : ListAdapter<FurnaceItem, CollectionViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FurnaceItem>() {
            override fun areItemsTheSame(oldItem: FurnaceItem, newItem: FurnaceItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FurnaceItem, newItem: FurnaceItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

