package com.example.todoapp.presentation.onboarding


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingPage(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)