package com.alonsonya.dailyfurnace.di

import com.alonsonya.dailyfurnace.data.repo.ProductsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { ProductsRepository(get()) }
}