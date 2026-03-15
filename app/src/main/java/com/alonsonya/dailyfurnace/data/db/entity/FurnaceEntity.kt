package com.alonsonya.dailyfurnace.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furnaces")
data class FurnaceEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val imageUrl: String?,
    val thumbnailUrl: String?
)
