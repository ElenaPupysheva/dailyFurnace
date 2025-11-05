package com.alonsonya.dailyfurnace.furnace.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.ProductDto
import com.alonsonya.dailyfurnace.data.repo.ProductsRepository
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FurnaceUiState(
    val loading: Boolean = false,
    val product: ProductDto? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)

// FurnaceViewModel.kt
class FurnaceViewModel(
    private val products: ProductsRepository,
    private val favorites: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FurnaceUiState())
    val state: StateFlow<FurnaceUiState> = _state

    private var favJob: Job? = null
    private var currentId: Int? = null

    fun loadStartup(argId: Int) {
        if (argId != -1) {
            loadById(argId)
        } else {
            viewModelScope.launch {
                _state.update { it.copy(loading = true, error = null) }
                runCatching { products.getFirstProductOrNull() }
                    .onSuccess { first ->
                        if (first != null) {
                            loadById(first.id) // переиспользуем основную ветку
                        } else {
                            _state.update { it.copy(loading = false, error = "Пустой список продуктов") }
                        }
                    }
                    .onFailure { e ->
                        _state.update { it.copy(loading = false, error = e.message ?: "Network error") }
                    }
            }
        }
    }

    fun loadById(id: Int) {
        if (currentId == id && _state.value.product != null) return
        currentId = id

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val product = products.getProduct(id)
                _state.update { it.copy(loading = false, product = product, error = null) }

                favJob?.cancel()
                favJob = launch {
                    favorites.isFavoriteFlow(id).collect { isFav ->
                        _state.update { it.copy(isFavorite = isFav) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "Network error") }
            }
        }
    }

    fun toggleFavorite() {
        val id = currentId ?: return
        viewModelScope.launch { favorites.toggle(id) }
    }
}