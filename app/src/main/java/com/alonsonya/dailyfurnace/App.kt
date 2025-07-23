package com.alonsonya.dailyfurnace

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.alonsonya.dailyfurnace.data.FURNACE_PREFERENCES
import com.alonsonya.dailyfurnace.data.SWITCH_KEY
import com.alonsonya.dailyfurnace.di.settingsModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    private lateinit var themePrefs: SharedPreferences
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        themePrefs = getSharedPreferences(FURNACE_PREFERENCES, MODE_PRIVATE)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    settingsModule
                )
            )
        }

        if (!themePrefs.contains(SWITCH_KEY)) {
            val isSystemDarkTheme =
                (resources.configuration.uiMode and
                        android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                        android.content.res.Configuration.UI_MODE_NIGHT_YES

            themePrefs.edit()
                .putBoolean(SWITCH_KEY, isSystemDarkTheme)
                .apply()
        }

        darkTheme = themePrefs.getBoolean(SWITCH_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        themePrefs.edit()
            .putBoolean(SWITCH_KEY, darkTheme)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}