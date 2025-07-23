package com.alonsonya.dailyfurnace.settings.data

import android.content.SharedPreferences
import com.alonsonya.dailyfurnace.data.SWITCH_KEY
import com.alonsonya.dailyfurnace.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_KEY, false)
    }

    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(SWITCH_KEY, enabled)
            .apply()
    }
}