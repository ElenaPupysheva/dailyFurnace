package com.alonsonya.dailyfurnace.di

import android.content.Context
import android.content.SharedPreferences
import com.alonsonya.dailyfurnace.data.FURNACE_PREFERENCES
import com.alonsonya.dailyfurnace.settings.data.SettingsRepositoryImpl
import com.alonsonya.dailyfurnace.settings.domain.SettingsInteractor
import com.alonsonya.dailyfurnace.settings.domain.SettingsInteractorImpl
import com.alonsonya.dailyfurnace.settings.domain.SettingsRepository
import com.alonsonya.dailyfurnace.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val settingsModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(FURNACE_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<SettingsInteractor> { SettingsInteractorImpl(get()) }

    viewModel { SettingsViewModel(get()) }

}