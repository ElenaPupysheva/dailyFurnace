package com.alonsonya.dailyfurnace.data.db.dao

import androidx.room.Query
import androidx.room.Update
import com.alonsonya.dailyfurnace.data.db.entity.FurnaceEntity

interface FurnacesDao {
    @Update
    suspend fun updatePlaylist(playlist: FurnaceEntity)

    @Query("SELECT * FROM furnaces")
    suspend fun getAllFurnaces(): List<FurnaceEntity>

    @Query("SELECT * FROM furnaces")
    fun getAllFurnacesFlow(): kotlinx.coroutines.flow.Flow<List<FurnaceEntity>>

    @Query("SELECT * FROM furnaces WHERE furnaceId = :id")
    suspend fun getFurnaceById(id: Long): FurnaceEntity?
}