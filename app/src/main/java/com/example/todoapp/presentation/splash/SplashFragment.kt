package com.example.todoapp.presentation.splash

import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.SplashBinding
import com.example.todoapp.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashBinding, SplashViewModel>(
    SplashBinding::inflate,
    SplashViewModel::class.java
) {

    override fun onViewReady() {
    }

    override fun observeViewModel() {
        observeNavigation()
    }

    private fun observeNavigation() {
        // Ab yahan hamara banaya hua clean extension function use ho raha hai
        observeFlow(viewModel.navigationState) { event ->
            when (event) {
                SplashEvents.NavigateToOnboarding ->
                    findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                SplashEvents.NavigateToHome ->
                    findNavController().navigate(R.id.action_splashFragment_to_todoFragment)
            }
        }
    }
}