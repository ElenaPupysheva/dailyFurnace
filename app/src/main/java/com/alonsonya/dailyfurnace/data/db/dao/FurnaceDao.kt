package com.alonsonya.dailyfurnace.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alonsonya.dailyfurnace.data.db.entity.FurnaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FurnaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FurnaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FurnaceEntity)

    @Query("SELECT * FROM furnaces ORDER BY id ASC")
    fun observeAll(): Flow<List<FurnaceEntity>>

    @Query(
        """
        SELECT * FROM furnaces
        WHERE title LIKE '%' || :query || '%'
           OR shortDescription LIKE '%' || :query || '%'
        ORDER BY id ASC
    """
    )
    fun observeSearch(query: String): Flow<List<FurnaceEntity>>

    @Query("SELECT * FROM furnaces WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FurnaceEntity?

    @Query("SELECT * FROM furnaces WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<FurnaceEntity?>

    @Query("DELETE FROM furnaces")
    suspend fun clearAll()
}