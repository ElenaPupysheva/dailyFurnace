package com.alonsonya.dailyfurnace.furnace.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FurnaceViewModel (
    private val favorites: FavoritesRepository
) : ViewModel() {

    fun toggleFavorite(id: Int) {
        viewModelScope.launch { favorites.toggle(id) }
    }

    fun isFavoriteFlow(id: Int): Flow<Boolean> = favorites.isFavoriteFlow(id)
}