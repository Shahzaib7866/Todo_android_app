package com.example.todoapp.core.cache

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val key = intPreferencesKey("theme_mode")


    val themeMode: Flow<Int> = context.dataStore.data.map { prefs ->
        if (prefs[key] == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
    }

    suspend fun setThemeMode(mode: Int) {
        context.dataStore.edit { it[key] = mode }
    }
}