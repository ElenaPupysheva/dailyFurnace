package com.alonsonya.dailyfurnace.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.AppError
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
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class FavoriteUiItem(
    val id: Int,
    val name: String,
    val imageUrl: String?
)

data class FavoriteUiState(
    val loading: Boolean = false,
    val items: List<FavoriteUiItem> = emptyList(),
    val error: AppError? = null
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
                        emit(
                            FavoriteUiState(
                                loading = false,
                                items = emptyList(),
                                error = null
                            )
                        )
                        return@flow
                    }

                    emit(FavoriteUiState(loading = true))

                    val results = coroutineScope {
                        ids.map { id ->
                            async {
                                runCatching { furnacesRepo.getFurnace(id) }
                            }
                        }.awaitAll()
                    }

                    val successfulDtos = results.mapNotNull { it.getOrNull() }
                    val firstError = results.firstNotNullOfOrNull { it.exceptionOrNull() }

                    if (successfulDtos.isEmpty() && firstError != null) {
                        emit(
                            FavoriteUiState(
                                loading = false,
                                items = emptyList(),
                                error = mapError(firstError)
                            )
                        )
                        return@flow
                    }

                    val uiItems = successfulDtos
                        .sortedBy { it.id }
                        .map { furnace ->
                            FavoriteUiItem(
                                id = furnace.id,
                                name = furnace.title,
                                imageUrl = furnace.imageUrl ?: furnace.thumbnailUrl
                            )
                        }

                    emit(
                        FavoriteUiState(
                            loading = false,
                            items = uiItems,
                            error = null
                        )
                    )
                }
                    .catch { e ->
                        emit(
                            FavoriteUiState(
                                loading = false,
                                items = emptyList(),
                                error = mapError(e)
                            )
                        )
                    }
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

    private fun mapError(throwable: Throwable): AppError {
        return when (throwable) {
            is UnknownHostException,
            is SocketTimeoutException,
            is IOException -> AppError.NoInternet

            is HttpException -> {
                when (throwable.code()) {
                    404 -> AppError.NotFound
                    500, 502, 503, 504 -> AppError.Server
                    else -> AppError.Unknown
                }
            }

            else -> AppError.Unknown
        }
    }
}