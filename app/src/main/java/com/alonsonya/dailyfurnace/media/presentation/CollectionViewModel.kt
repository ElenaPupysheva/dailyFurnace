package com.alonsonya.dailyfurnace.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor

data class CollectionUiState(
    val collectionList: List<Furnace> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
class CollectionViewModel(
    private val collectionInteractor: CollectionInteractor
) : ViewModel() {
    private val _uiState = MutableLiveData(CollectionUiState())
    val uiState: LiveData<CollectionUiState> = _uiState

}