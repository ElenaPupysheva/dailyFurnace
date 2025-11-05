package com.alonsonya.dailyfurnace.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alonsonya.dailyfurnace.data.db.entity.FavoriteFurnaceEntity
import com.alonsonya.dailyfurnace.data.db.dao.FavoriteFurnaceDao

@Database(
    entities = [FavoriteFurnaceEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteFurnaceDao(): FavoriteFurnaceDao

}