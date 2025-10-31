package com.alonsonya.dailyfurnace

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.alonsonya.dailyfurnace.data.FURNACE_PREFERENCES
import com.alonsonya.dailyfurnace.data.SWITCH_KEY
import com.alonsonya.dailyfurnace.di.databaseModule
import com.alonsonya.dailyfurnace.di.networkModule
import com.alonsonya.dailyfurnace.di.settingsModule
import com.alonsonya.dailyfurnace.favorite.di.favoriteModule
import com.alonsonya.dailyfurnace.furnace.di.furnaceModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    private lateinit var themePrefs: SharedPreferences
    var darkTheme = false
    private val CHANNEL_FACTS = "facts_channel"
    override fun onCreate() {
        super.onCreate()
        themePrefs = getSharedPreferences(FURNACE_PREFERENCES, MODE_PRIVATE)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    furnaceModule,
                    settingsModule,
                    favoriteModule,
                    databaseModule,
                    networkModule
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
        createNotificationChannel()
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                CHANNEL_FACTS,
                "Печь дня",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления с ежедневными фактами"
            }
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(ch)
        }
    }
}