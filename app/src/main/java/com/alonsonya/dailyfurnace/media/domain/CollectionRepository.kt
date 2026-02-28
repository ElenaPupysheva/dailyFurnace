package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem

interface CollectionRepository {
    suspend fun getFurnace(furnaceId: Int): Furnace
    suspend fun getFurnaceList(): List<Furnace>
    suspend fun getFurnacesPage(limit: Int, offset: Int): List<FurnaceItem>
    suspend fun searchFurnaces(query: String, limit: Int, offset: Int): List<FurnaceItem>
}
