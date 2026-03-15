package com.alonsonya.dailyfurnace.media.domain

import com.alonsonya.dailyfurnace.data.Furnace
import com.alonsonya.dailyfurnace.data.FurnaceItem
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    fun observeFurnaces(): Flow<List<FurnaceItem>>

    fun observeSearch(query: String): Flow<List<FurnaceItem>>

    fun observeFurnaceDetails(furnaceId: Int): Flow<Furnace?>

    suspend fun syncFurnacesPage(limit: Int, offset: Int): Int

    suspend fun syncFurnaceDetails(furnaceId: Int)
}