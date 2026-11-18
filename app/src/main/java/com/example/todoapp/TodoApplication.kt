package com.example.todoapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.core.cache.ThemeDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application() {

    @Inject lateinit var themeDataStore: ThemeDataStore

    override fun onCreate() {
        super.onCreate()
        val mode = runBlocking { themeDataStore.themeMode.first() }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}