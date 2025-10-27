package com.alonsonya.dailyfurnace.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.mockFurnaces
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel( private val favorites: FavoritesRepository
) : ViewModel() {

    // Пока берём печи из mockFurnaces и фильтруем по избранным ID.
    // Когда подключите бэкенд — заменишь фильтр на запрос «получить печи по списку ID».
    val items = favorites.observeAllIds()
        .map { ids ->
            val set = ids.toSet()
            mockFurnaces
                .filter { it.furnaceId in set }
                .map { it.copy(isFavorite = true) }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList<Furnace>())
}