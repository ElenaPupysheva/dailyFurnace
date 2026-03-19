package com.alonsonya.dailyfurnace.furnace.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.data.FurnaceDto
import com.alonsonya.dailyfurnace.data.repo.FurnacesRepository
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class FurnaceUiState(
    val loading: Boolean = false,
    val furnace: FurnaceDto? = null,
    val isFavorite: Boolean = false,
    val error: AppError? = null
)

class FurnaceViewModel(
    private val furnaces: FurnacesRepository,
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
                runCatching { furnaces.getDailyFurnace() }
                    .onSuccess { daily ->
                        currentId = daily.id
                        _state.update { it.copy(loading = false, furnace = daily, error = null) }
                        observeFavorite(daily.id)
                    }
                    .onFailure { e ->
                        _state.update {
                            it.copy(
                                loading = false,
                                furnace = null,
                                error = mapError(e)
                            )
                        }
                    }
            }
        }
    }

    fun loadById(id: Int) {
        if (currentId == id && _state.value.furnace != null) return
        currentId = id

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { furnaces.getFurnace(id) }
                .onSuccess { dto ->
                    _state.update { it.copy(loading = false, furnace = dto, error = null) }
                    observeFavorite(id)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            loading = false,
                            furnace = null,
                            error = mapError(e)
                        )
                    }
                }
        }
    }

    private fun observeFavorite(id: Int) {
        favJob?.cancel()
        favJob = viewModelScope.launch {
            favorites.isFavoriteFlow(id).collect { isFav ->
                _state.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        val id = currentId ?: return
        viewModelScope.launch { favorites.toggle(id) }
    }

    fun retry() {
        val id = currentId
        if (id != null) {
            loadById(id)
        } else {
            loadStartup(-1)
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