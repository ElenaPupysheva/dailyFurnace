package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace

interface CollectionRepository {
    suspend fun getFurnace(furnaceId: Int): Furnace
    suspend fun getFurnaceList(): List<Furnace>
}
