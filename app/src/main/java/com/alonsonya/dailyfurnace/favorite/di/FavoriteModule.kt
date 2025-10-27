package com.alonsonya.dailyfurnace.favorite.di

import com.alonsonya.dailyfurnace.favorite.data.FavoritesRepositoryImpl
import com.alonsonya.dailyfurnace.favorite.domain.FavoritesRepository
import com.alonsonya.dailyfurnace.favorite.presentation.FavoritesViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    viewModel { FavoritesViewModel(get()) }
}