package com.alonsonya.dailyfurnace.favorite.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.ItemFurnaceBinding
import com.alonsonya.dailyfurnace.favorite.presentation.FavoriteUiItem

class FurnaceAdapter(
    private val onClick: (FavoriteUiItem) -> Unit,
    private val onHeartClick: (FavoriteUiItem) -> Unit
) : ListAdapter<FavoriteUiItem, FurnaceAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<FavoriteUiItem>() {
        override fun areItemsTheSame(old: FavoriteUiItem, new: FavoriteUiItem) = old.id == new.id
        override fun areContentsTheSame(old: FavoriteUiItem, new: FavoriteUiItem) = old == new
    }

    inner class VH(val binding: ItemFurnaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteUiItem) = with(binding) {
            furnaceName.text = item.name
            furnaceImage.load(item.imageUrl) {
                placeholder(R.drawable.fire)
                error(R.drawable.error)
                crossfade(true)
            }

            root.setOnClickListener { onClick(item) }
            heartMinus.setOnClickListener { onHeartClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        return VH(ItemFurnaceBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}