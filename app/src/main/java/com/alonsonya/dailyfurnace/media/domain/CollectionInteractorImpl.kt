package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem
import kotlinx.coroutines.flow.Flow

class CollectionInteractorImpl(
    private val repository: CollectionRepository
) : CollectionInteractor {

    override fun observeFurnaces(): Flow<List<FurnaceItem>> {
        return repository.observeFurnaces()
    }

    override fun observeSearch(query: String): Flow<List<FurnaceItem>> {
        return repository.observeSearch(query)
    }

    override fun observeFurnaceDetails(furnaceId: Int): Flow<Furnace?> {
        return repository.observeFurnaceDetails(furnaceId)
    }

    override suspend fun syncFurnacesPage(limit: Int, offset: Int): Int {
        return repository.syncFurnacesPage(limit, offset)
    }

    override suspend fun syncFurnaceDetails(furnaceId: Int) {
        repository.syncFurnaceDetails(furnaceId)
    }
}