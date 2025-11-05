package com.alonsonya.dailyfurnace.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_furnaces")
data class FavoriteFurnaceEntity(
    @PrimaryKey val furnaceId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
