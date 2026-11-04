package com.shahzy.todoapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahzy.todoapp.core.shared.datastore.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingDataStore
) : ViewModel() {

    private val _splashEvent = Channel<SplashEvent>()
    val splashEvent = _splashEvent.receiveAsFlow()

    init {
        checkSplash()
    }

    fun checkSplash() {
        viewModelScope.launch {
            // 2 seconds delay for splash
            delay(2000.milliseconds)

            val complete = onboardingPreferences.isOnboardingCompleted.first()

            if (complete) {
                _splashEvent.send(SplashEvent.OnNavigateToHome)
            } else {
                _splashEvent.send(SplashEvent.OnNavigateToOnBoarding)
            }
        }
    }
}

sealed class SplashEvent {
    data object OnNavigateToHome : SplashEvent()
    data object OnNavigateToOnBoarding : SplashEvent()
}
