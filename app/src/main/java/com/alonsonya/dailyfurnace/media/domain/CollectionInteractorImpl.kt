package com.alonsonya.dailyfurnace.media.domain

import androidx.lifecycle.LiveData
import com.alonsonya.dailyfurnace.data.Furnace

class CollectionInteractorImpl(
    private val repository: CollectionRepository
): CollectionInteractor {
    override fun getFurnace(furnaceId: Int): Furnace {
        return repository.getFurnace(furnaceId)
    }

    override fun getFurnaceList(): LiveData<List<Furnace>> {
        return repository.getFurnaceList()
    }
}