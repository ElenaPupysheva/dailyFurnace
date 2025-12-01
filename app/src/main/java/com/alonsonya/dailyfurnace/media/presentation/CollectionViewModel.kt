package com.alonsonya.dailyfurnace.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CollectionUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val items: List<Furnace> = emptyList(),
)

class CollectionViewModel(
    private val collectionInteractor: CollectionInteractor
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state

    init {
        loadCollection()
    }

    fun loadCollection() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                error = null
            )

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    collectionInteractor.getFurnaceList()
                }
            }

            result.onSuccess { furnaces: List<Furnace> ->
                _state.value = _state.value.copy(
                    loading = false,
                    error = null,
                    items = furnaces.sortedBy { it.furnaceId }
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Network error"
                )
            }
        }
    }
}