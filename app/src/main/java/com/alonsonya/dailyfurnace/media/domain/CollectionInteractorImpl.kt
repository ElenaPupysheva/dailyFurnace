package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem

class CollectionInteractorImpl(
    private val repository: CollectionRepository
) : CollectionInteractor {
    override suspend fun getFurnace(furnaceId: Int): Furnace {
        return repository.getFurnace(furnaceId)
    }

    override suspend fun getFurnaceList(): List<Furnace> {
        return repository.getFurnaceList()
    }

    override suspend fun getFurnacePage(limit: Int, offset: Int): List<FurnaceItem> {
        return repository.getFurnacesPage(limit, offset)
    }

    override suspend fun searchFurnaces(
        query: String,
        limit: Int,
        offset: Int
    ): List<FurnaceItem> {
        return repository.searchFurnaces(query, limit, offset)
    }
}