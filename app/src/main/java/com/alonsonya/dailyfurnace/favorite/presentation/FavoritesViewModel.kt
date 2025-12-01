package com.alonsonya.dailyfurnace.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.repo.ProductsRepository
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn

data class FavoriteUiItem(val id: Int, val name: String, val imageUrl: String?)
data class FavoriteUiState(
    val loading: Boolean = false,
    val items: List<FavoriteUiItem> = emptyList(),
    val error: String? = null
)

class FavoritesViewModel(
    private val favorites: FavoritesRepository,
    private val productsRepo: ProductsRepository
) : ViewModel() {

    private val favIdsFlow = favorites.observeAllIds()
        .map { it.toSet() }
        .distinctUntilChanged()

    val state: StateFlow<FavoriteUiState> =
        favIdsFlow
            .sample(300)
            .flatMapLatest { ids: Set<Int> ->
                flow {
                    if (ids.isEmpty()) {
                        emit(FavoriteUiState(items = emptyList()))
                        return@flow
                    }

                    emit(FavoriteUiState(loading = true))

                    val list = try {
                        productsRepo.getByIds(ids.toList())
                    } catch (_: Throwable) {
                        productsRepo.getByIdsFallback(ids.toList())
                    }

                    val ui = list
                        .sortedBy { it.id }
                        .map { p -> FavoriteUiItem(p.id, p.name, p.image_url) }

                    emit(FavoriteUiState(items = ui))
                }
                    .catch { e -> emit(FavoriteUiState(error = e.message ?: "Network error")) }
                    .flowOn(Dispatchers.IO)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                FavoriteUiState()
            )
}