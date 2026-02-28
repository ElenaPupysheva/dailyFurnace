package com.alonsonya.dailyfurnace.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

data class CollectionUiState(
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val endReached: Boolean = false,
    val error: String? = null,
    val items: List<FurnaceItem> = emptyList(),
)

class CollectionViewModel(
    private val collectionInteractor: CollectionInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state

    private val limit = 20

    // -------- FEED (обычная лента) --------
    private var feedOffset = 0
    private var feedEndReached = false
    private var feedItemsCache: List<FurnaceItem> = emptyList()
    private var feedInFlight = false

    // -------- SEARCH (поиск) --------
    private var currentQuery: String = ""
    private var searchOffset = 0
    private var searchEndReached = false
    private var searchItemsCache: List<FurnaceItem> = emptyList()
    private var searchInFlight = false

    private var searchJob: Job? = null

    // Настройки UX
    private val debounceMs = 600L
    private val minQueryLength = 2

    fun loadFirstPage() {
        if (feedInFlight) return

        feedOffset = 0
        feedEndReached = false

        val shouldUpdateUi = currentQuery.isBlank()

        if (shouldUpdateUi) {
            _state.update {
                it.copy(
                    loading = true,
                    loadingMore = false,
                    endReached = false,
                    error = null
                )
            }
        }

        viewModelScope.launch {
            feedInFlight = true
            try {
                val page = collectionInteractor.getFurnacePage(limit, feedOffset)

                feedItemsCache = page
                feedEndReached = page.size < limit
                feedOffset += page.size

                if (shouldUpdateUi) {
                    _state.update {
                        it.copy(
                            loading = false,
                            items = feedItemsCache,
                            endReached = feedEndReached,
                            error = null
                        )
                    }
                } else {
                    _state.update { it.copy(loading = false) }
                }
            } catch (e: Exception) {
                if (e is CancellationException) return@launch
                if (shouldUpdateUi) {
                    _state.update {
                        it.copy(
                            loading = false,
                            error = e.message ?: "Network error"
                        )
                    }
                }
            } finally {
                feedInFlight = false
            }
        }
    }

    fun loadNextPage() {
        val s = _state.value
        if (feedInFlight || s.loading || s.loadingMore || feedEndReached) return

        val shouldUpdateUi = currentQuery.isBlank()

        if (shouldUpdateUi) {
            _state.update { it.copy(loadingMore = true, error = null) }
        }

        viewModelScope.launch {
            feedInFlight = true
            try {
                val page = collectionInteractor.getFurnacePage(limit, feedOffset)

                feedItemsCache = feedItemsCache + page
                feedEndReached = page.size < limit
                feedOffset += page.size

                if (shouldUpdateUi) {
                    _state.update {
                        it.copy(
                            loadingMore = false,
                            items = feedItemsCache,
                            endReached = feedEndReached,
                            error = null
                        )
                    }
                } else {
                    _state.update { it.copy(loadingMore = false) }
                }
            } catch (e: Exception) {
                if (e is CancellationException) return@launch
                if (shouldUpdateUi) {
                    _state.update {
                        it.copy(
                            loadingMore = false,
                            error = e.message ?: "Network error"
                        )
                    }
                }
            } finally {
                feedInFlight = false
            }
        }
    }

    fun onSearchQueryChanged(text: String) {
        val q = text.trim()
        if (q == currentQuery) return

        currentQuery = q
        searchJob?.cancel()

        if (q.isBlank()) {
            _state.update {
                it.copy(
                    loading = false,
                    loadingMore = false,
                    items = feedItemsCache,
                    endReached = feedEndReached,
                    error = null
                )
            }
            return
        }

        if (q.length < minQueryLength) {
            _state.update {
                it.copy(
                    loading = false,
                    loadingMore = false,
                    items = emptyList(),
                    endReached = true,
                    error = null
                )
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(debounceMs)
            searchFirstPage()
        }
    }

    fun loadNext() {
        if (currentQuery.isBlank()) {
            loadNextPage()
        } else {
            viewModelScope.launch { loadNextSearchPage() }
        }
    }

    private suspend fun searchFirstPage() {
        if (searchInFlight) return

        searchOffset = 0
        searchEndReached = false
        searchItemsCache = emptyList()

        _state.update {
            it.copy(
                loading = true,
                loadingMore = false,
                endReached = false,
                error = null,
                items = emptyList()
            )
        }

        searchInFlight = true
        try {
            val page = collectionInteractor.searchFurnaces(currentQuery, limit, searchOffset)

            searchItemsCache = page
            searchEndReached = page.size < limit
            searchOffset += page.size

            _state.update {
                it.copy(
                    loading = false,
                    items = searchItemsCache,
                    endReached = searchEndReached,
                    error = null
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) return
            if (e is IOException) {
                val msg = e.message.orEmpty()
                if (msg.contains("Canceled", ignoreCase = true) || msg.contains("Socket closed", ignoreCase = true)) return
            }

            _state.update {
                it.copy(
                    loading = false,
                    error = e.message ?: "Network error"
                )
            }
        } finally {
            searchInFlight = false
        }
    }

    private suspend fun loadNextSearchPage() {
        val s = _state.value
        if (searchInFlight || s.loading || s.loadingMore || searchEndReached) return

        _state.update { it.copy(loadingMore = true, error = null) }

        searchInFlight = true
        try {
            val page = collectionInteractor.searchFurnaces(currentQuery, limit, searchOffset)

            searchItemsCache = searchItemsCache + page
            searchEndReached = page.size < limit
            searchOffset += page.size

            _state.update {
                it.copy(
                    loadingMore = false,
                    items = searchItemsCache,
                    endReached = searchEndReached,
                    error = null
                )
            }
        } catch (e: Exception) {
            if (e is CancellationException) return
            if (e is IOException) {
                val msg = e.message.orEmpty()
                if (msg.contains("Canceled", ignoreCase = true) || msg.contains("Socket closed", ignoreCase = true)) return
            }

            _state.update {
                it.copy(
                    loadingMore = false,
                    error = e.message ?: "Network error"
                )
            }
        } finally {
            searchInFlight = false
        }
    }
}