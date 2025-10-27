package com.alonsonya.dailyfurnace.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alonsonya.dailyfurnace.data.db.entity.FavoriteFurnaceEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoriteFurnaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteFurnaceEntity): Long

    @Query("DELETE FROM favorite_furnaces WHERE furnaceId = :id")
    suspend fun deleteById(id: Int): Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_furnaces WHERE furnaceId = :id)")
    fun isFavoriteFlow(id: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_furnaces WHERE furnaceId = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("SELECT furnaceId FROM favorite_furnaces ORDER BY addedAt DESC")
    fun observeAllIds(): Flow<List<Int>>
}