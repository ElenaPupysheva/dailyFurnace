package com.alonsonya.dailyfurnace.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alonsonya.dailyfurnace.data.db.entity.FavoriteFurnaceEntity
import com.alonsonya.dailyfurnace.data.db.dao.FavoriteFurnaceDao
import com.alonsonya.dailyfurnace.data.db.dao.FurnacesDao
import com.alonsonya.dailyfurnace.data.db.entity.FurnaceEntity

@Database(
    entities = [FavoriteFurnaceEntity::class, FurnaceEntity::class],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteFurnaceDao(): FavoriteFurnaceDao
    abstract fun furnacesDao(): FurnacesDao

}