package com.alonsonya.dailyfurnace.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.repo.FurnacesRepository
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteUiItem(val id: Int, val name: String, val imageUrl: String?)
data class FavoriteUiState(
    val loading: Boolean = false,
    val items: List<FavoriteUiItem> = emptyList(),
    val error: String? = null
)

class FavoritesViewModel(
    private val favorites: FavoritesRepository,
    private val furnacesRepo: FurnacesRepository
) : ViewModel() {

    private val favIdsFlow = favorites.observeAllIds()
        .map { it.toSet() }
        .distinctUntilChanged()

    val state: StateFlow<FavoriteUiState> =
        favIdsFlow
            .debounce(200)
            .flatMapLatest { ids: Set<Int> ->
                flow {
                    if (ids.isEmpty()) {
                        emit(FavoriteUiState(items = emptyList()))
                        return@flow
                    }

                    emit(FavoriteUiState(loading = true))

                    val dtos = coroutineScope {
                        ids.map { id ->
                            async { runCatching { furnacesRepo.getFurnace(id) }.getOrNull() }
                        }.awaitAll().filterNotNull()
                    }

                    val ui = dtos
                        .sortedBy { it.id }
                        .map { f ->
                            FavoriteUiItem(
                                id = f.id,
                                name = f.title,
                                imageUrl = f.imageUrl ?: f.thumbnailUrl
                            )
                        }

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


    fun removeFromFavorites(id: Int) {
        viewModelScope.launch {
            favorites.remove(id)
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            favorites.clearAll()
        }
    }
}