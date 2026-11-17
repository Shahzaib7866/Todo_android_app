package com.example.todoapp.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.core.cache.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    val themeMode: LiveData<Int> = themeDataStore.themeMode.asLiveData()

    fun setThemeMode(mode: Int) {
        viewModelScope.launch { themeDataStore.setThemeMode(mode) }
    }
}