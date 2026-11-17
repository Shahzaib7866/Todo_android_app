package com.example.todoapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.core.cache.OnboardingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.todoapp.R


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingDataStore
) : ViewModel() {


    // class ke andar, channel ke upar:
    val pages = listOf(
        OnBoardingPage(R.drawable.todo, R.string.onboarding_title_1, R.string.onboarding_desc_1),
        OnBoardingPage(R.drawable.todo, R.string.onboarding_title_2, R.string.onboarding_desc_2),
        OnBoardingPage(R.drawable.todo, R.string.onboarding_title_3, R.string.onboarding_desc_3)
    )

    // --- EVENT: Navigation aik one-time event hai, is liye SharedFlow use kiya hai ---
    private val _navigateToHome = MutableSharedFlow<Unit>()
    val navigateToHome: SharedFlow<Unit> = _navigateToHome.asSharedFlow()

    fun onGetStartedClicked() {
        viewModelScope.launch {
            onboardingPreferences.saveOnboardingCompleted(true)
            // Event emit kar diya, ab manually state reset karne ki zaroorat nahi
            _navigateToHome.emit(Unit)
        }
    }
}