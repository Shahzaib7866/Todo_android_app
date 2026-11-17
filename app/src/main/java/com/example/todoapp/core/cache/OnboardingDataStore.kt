package com.example.todoapp.core.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// File level par DataStore property bilkul theek hai
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_prefs")
@Singleton
class OnboardingDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Key ko companion object mein daal diya taake ye 'private static' rahay aur leak na ho
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }
    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[ONBOARDING_COMPLETED_KEY] ?: false
    }

    suspend fun saveOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }
}
