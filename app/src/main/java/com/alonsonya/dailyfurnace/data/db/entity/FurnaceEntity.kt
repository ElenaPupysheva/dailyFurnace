package com.alonsonya.dailyfurnace.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furnaces")
data class FurnaceEntity (
    @PrimaryKey(autoGenerate = true)
    val furnaceId: Int,
    val furnaceName: String,
    val furnaceInfo: String,
    val furnaceType: String,
    val imageRes: String?
)