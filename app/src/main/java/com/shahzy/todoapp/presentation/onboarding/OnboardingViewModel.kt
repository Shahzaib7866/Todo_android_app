package com.shahzy.todoapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahzy.todoapp.core.shared.datastore.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingDataStore
) : ViewModel() {

    private val _onboardingEvent = Channel<OnboardingEvent>()
    val onboardingEvent = _onboardingEvent.receiveAsFlow()

    fun onGetStartedClicked() {
        viewModelScope.launch {
            onboardingPreferences.saveOnboardingCompleted(true)
            _onboardingEvent.send(OnboardingEvent.NavigateToHome)
        }
    }
}

sealed class OnboardingEvent {
    data object NavigateToHome : OnboardingEvent()
}

