package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace

class CollectionInteractorImpl(
    private val repository: CollectionRepository
) : CollectionInteractor {
    override suspend fun getFurnace(furnaceId: Int): Furnace {
        return repository.getFurnace(furnaceId)
    }
    override suspend fun getFurnaceList(): List<Furnace> {
        return repository.getFurnaceList()
    }
}