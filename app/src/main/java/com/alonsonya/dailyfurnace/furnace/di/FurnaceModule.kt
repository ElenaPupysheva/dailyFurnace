package com.alonsonya.dailyfurnace.furnace.di

import com.alonsonya.dailyfurnace.furnace.presentation.FurnaceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val furnaceModule = module {
    viewModel { FurnaceViewModel(get()) }
}