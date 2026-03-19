package com.alonsonya.dailyfurnace.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.AppError
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class CollectionUiState(
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: AppError? = null,
    val items: List<FurnaceItem> = emptyList(),
    val query: String = ""
)

class CollectionViewModel(
    private val collectionInteractor: CollectionInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state

    private val limit = 20
    private val debounceMs = 600L
    private val minQueryLength = 2

    private var currentQuery: String = ""
    private var offset: Int = 0
    private var endReachedInternal: Boolean = false
    private var syncInFlight: Boolean = false

    private var observeJob: Job? = null
    private var searchJob: Job? = null

    init {
        observeFeed()
    }

    fun loadFirstPage() {
        if (syncInFlight) return

        offset = 0
        endReachedInternal = false

        _state.update {
            it.copy(
                loading = true,
                loadingMore = false,
                endReached = false,
                error = null
            )
        }

        viewModelScope.launch {
            syncInFlight = true
            try {
                val loadedCount = collectionInteractor.syncFurnacesPage(limit, 0)

                endReachedInternal = loadedCount < limit
                offset = loadedCount

                _state.update {
                    it.copy(
                        loading = false,
                        loadingMore = false,
                        endReached = endReachedInternal,
                        error = null
                    )
                }
            } catch (e: Exception) {
                if (e is CancellationException) return@launch

                _state.update {
                    it.copy(
                        loading = false,
                        loadingMore = false,
                        error = mapError(e)
                    )
                }
            } finally {
                syncInFlight = false
            }
        }
    }

    fun loadNext() {
        if (currentQuery.isNotBlank()) return
        if (syncInFlight) return
        if (_state.value.loading || _state.value.loadingMore || endReachedInternal) return

        _state.update {
            it.copy(
                loadingMore = true,
                error = null
            )
        }

        viewModelScope.launch {
            syncInFlight = true
            try {
                val loadedCount = collectionInteractor.syncFurnacesPage(limit, offset)

                endReachedInternal = loadedCount < limit
                offset += loadedCount

                _state.update {
                    it.copy(
                        loadingMore = false,
                        endReached = endReachedInternal,
                        error = null
                    )
                }
            } catch (e: Exception) {
                if (e is CancellationException) return@launch

                _state.update {
                    it.copy(
                        loadingMore = false,
                        error = mapError(e)
                    )
                }
            } finally {
                syncInFlight = false
            }
        }
    }

    fun onSearchQueryChanged(text: String) {
        val q = text.trim()
        if (q == currentQuery) return

        currentQuery = q
        searchJob?.cancel()

        _state.update { it.copy(query = q) }

        if (q.isBlank()) {
            observeFeed()
            _state.update {
                it.copy(
                    loading = false,
                    loadingMore = false,
                    error = null,
                    endReached = endReachedInternal,
                    query = q
                )
            }
            return
        }

        if (q.length < minQueryLength) {
            observeJob?.cancel()
            _state.update {
                it.copy(
                    items = emptyList(),
                    loading = false,
                    loadingMore = false,
                    endReached = true,
                    error = null,
                    query = q
                )
            }
            return
        }

        _state.update {
            it.copy(
                loading = true,
                loadingMore = false,
                items = emptyList(),
                endReached = true,
                error = null,
                query = q
            )
        }

        searchJob = viewModelScope.launch {
            delay(debounceMs)
            observeSearch(q)
        }
    }

    fun retry() {
        if (currentQuery.isBlank()) {
            loadFirstPage()
        } else {
            if (currentQuery.length < minQueryLength) return

            _state.update {
                it.copy(
                    loading = true,
                    loadingMore = false,
                    items = emptyList(),
                    endReached = true,
                    error = null,
                    query = currentQuery
                )
            }
            observeSearch(currentQuery)
        }
    }

    private fun observeFeed() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            collectionInteractor.observeFurnaces()
                .catch { e ->
                    _state.update {
                        it.copy(
                            loading = false,
                            loadingMore = false,
                            error = mapError(e)
                        )
                    }
                }
                .collect { items ->
                    _state.update {
                        it.copy(
                            items = items,
                            loading = false,
                            loadingMore = false,
                            endReached = endReachedInternal,
                            error = null
                        )
                    }
                }
        }
    }

    private fun observeSearch(query: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            collectionInteractor.observeSearch(query)
                .catch { e ->
                    _state.update {
                        it.copy(
                            loading = false,
                            loadingMore = false,
                            error = mapError(e)
                        )
                    }
                }
                .collect { items ->
                    _state.update {
                        it.copy(
                            items = items,
                            loading = false,
                            loadingMore = false,
                            endReached = true,
                            error = null
                        )
                    }
                }
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