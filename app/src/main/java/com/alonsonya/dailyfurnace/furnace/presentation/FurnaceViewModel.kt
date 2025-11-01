package com.alonsonya.dailyfurnace.furnace.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.ProductDto
import com.alonsonya.dailyfurnace.data.repo.ProductsRepository
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FurnaceUiState(
    val loading: Boolean = false,
    val product: ProductDto? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)
class FurnaceViewModel(
    private val products: ProductsRepository,
    private val favorites: FavoritesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FurnaceUiState())
    val state: StateFlow<FurnaceUiState> = _state

    private var favJob: Job? = null
    private var currentId: Int? = null

    fun loadById(id: Int) {
        if (currentId == id && _state.value.product != null) return
        currentId = id

        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            runCatching { products.getProduct(id) }
                .onSuccess { product ->
                    _state.value = _state.value.copy(loading = false, product = product, error = null)
                    favJob?.cancel()
                    favJob = launch {
                        favorites.isFavoriteFlow(id).collect { isFav ->
                            _state.value = _state.value.copy(isFavorite = isFav)
                        }
                    }
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(loading = false, error = e.message)
                }
        }
    }

    fun toggleFavorite() {
        val id = currentId ?: return
        viewModelScope.launch { favorites.toggle(id) }
    }
}