package com.alonsonya.dailyfurnace.media.domain

import androidx.lifecycle.LiveData
import com.alonsonya.dailyfurnace.data.Furnace
import kotlinx.coroutines.flow.Flow

interface CollectionInteractor {
    suspend fun getFurnace(furnaceId: Int): Furnace
    suspend fun getFurnaceList(): List<Furnace>
}