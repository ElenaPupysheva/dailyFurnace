package com.alonsonya.dailyfurnace.di

import com.alonsonya.dailyfurnace.media.data.CollectionRepositoryImpl
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractor
import com.alonsonya.dailyfurnace.media.domain.CollectionInteractorImpl
import com.alonsonya.dailyfurnace.media.domain.CollectionRepository
import com.alonsonya.dailyfurnace.media.presentation.CollectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val collectionModule = module {

    single<CollectionRepository> {
        CollectionRepositoryImpl(
            furnacesRepository = get(),
            furnaceDao = get()
        )
    }

    single<CollectionInteractor> {
        CollectionInteractorImpl(get())
    }

    viewModel {
        CollectionViewModel(
            collectionInteractor = get()
        )
    }
}