package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem

interface CollectionInteractor {
    suspend fun getFurnace(furnaceId: Int): Furnace
    suspend fun getFurnaceList(): List<Furnace>
    suspend fun getFurnacePage(limit: Int, offset: Int): List<FurnaceItem>
}