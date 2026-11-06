package com.example.todoapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.core.shared.datastore.OnboardingDataStore
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

    private val _splashEvent = MutableSharedFlow<SplashEvent>()
    val splashEvent: SharedFlow<SplashEvent> = _splashEvent.asSharedFlow()

    init {
        checkSplash()
    }

    fun checkSplash() {
        viewModelScope.launch {
            // 2 seconds delay for splash
            delay(2000.milliseconds)

            val complete = onboardingPreferences.isOnboardingCompleted.first()

            if (complete) {
                _splashEvent.emit(SplashEvent.OnNavigateToHome)
            } else {
                _splashEvent.emit(SplashEvent.OnNavigateToOnBoarding)
            }
        }
    }
}

sealed class SplashEvent {
    object OnNavigateToHome : SplashEvent()
    object OnNavigateToOnBoarding : SplashEvent()
}