package com.example.todoapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.core.cache.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingDataStore
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<SplashEvents>()
    val navigationState: SharedFlow<SplashEvents> = _navigationEvents.asSharedFlow()

    init {
        checkSplash()
    }

    private fun checkSplash() {
        viewModelScope.launch {
            delay(2000.milliseconds)
            val complete = onboardingPreferences.isOnboardingCompleted.first()

            if (complete) {
                _navigationEvents.emit(SplashEvents.NavigateToHome)
            } else {
                _navigationEvents.emit(SplashEvents.NavigateToOnboarding)
            }
        }
    }
}

sealed class SplashEvents {
    data object NavigateToHome : SplashEvents()
    data object NavigateToOnboarding : SplashEvents()
}