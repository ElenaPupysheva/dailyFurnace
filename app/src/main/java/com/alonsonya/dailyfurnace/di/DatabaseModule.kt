package com.alonsonya.dailyfurnace.di

import androidx.room.Room
import com.alonsonya.dailyfurnace.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "daily_furnace.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<AppDatabase>().furnaceDao() }
    single { get<AppDatabase>().favoriteFurnaceDao() }
}