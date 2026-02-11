package com.alonsonya.dailyfurnace.di

import com.alonsonya.dailyfurnace.data.repo.FurnacesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { FurnacesRepository(get()) }
}