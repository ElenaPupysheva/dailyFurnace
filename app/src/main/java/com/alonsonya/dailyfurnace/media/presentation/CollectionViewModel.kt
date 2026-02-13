package com.alonsonya.dailyfurnace.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.FurnaceItem
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private var offset = 0
    private var inFlight = false

    fun loadFirstPage() {
        if (inFlight) return
        offset = 0

        _state.update {
            it.copy(
                loading = true,
                loadingMore = false,
                endReached = false,
                error = null
            )
        }

        viewModelScope.launch {
            inFlight = true
            runCatching { collectionInteractor.getFurnacePage(limit, offset) }
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            loading = false,
                            items = page,
                            endReached = page.size < limit,
                            error = null
                        )
                    }
                    offset += page.size
                }
                .onFailure { e ->
                    _state.update { it.copy(loading = false, error = e.message ?: "Network error") }
                }
            inFlight = false
        }
    }

    fun loadNextPage() {
        val s = _state.value
        if (inFlight || s.loading || s.loadingMore || s.endReached) return

        _state.update { it.copy(loadingMore = true, error = null) }

        viewModelScope.launch {
            inFlight = true
            runCatching { collectionInteractor.getFurnacePage(limit, offset) }
                .onSuccess { page ->
                    _state.update {
                        it.copy(
                            loadingMore = false,
                            items = it.items + page,
                            endReached = page.size < limit,
                            error = null
                        )
                    }
                    offset += page.size
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            loadingMore = false,
                            error = e.message ?: "Network error"
                        )
                    }
                }
            inFlight = false
        }
    }
}
