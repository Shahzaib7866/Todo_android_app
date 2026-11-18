package com.example.todoapp.presentation.onboarding


import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.OnBoardingBinding
import com.example.todoapp.core.shared.utils.observeFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java


@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<OnBoardingBinding, OnBoardingViewModel>(
    OnBoardingBinding::inflate,
    OnBoardingViewModel::class.java
) {

    override fun onViewReady() {
        setupPager()
        setupButtons()
        setupBackPress()
    }

    override fun observeViewModel() {
        observeNavigation()
    }

    private fun setupPager() {
        binding.viewPager.adapter = OnBoardingPagerAdapter(viewModel.pages)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) = updateButtons(position)
        })
        updateButtons(binding.viewPager.currentItem)
    }

    private fun updateButtons(position: Int) {
        val isLast = position == viewModel.pages.lastIndex
        binding.btnNext.text = if (isLast) getString(R.string.get_started) else getString(R.string.next)
        binding.btnSkip.isVisible = !isLast
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current == viewModel.pages.lastIndex) {
                viewModel.onGetStartedClicked()
            } else {
                binding.viewPager.currentItem = current + 1
            }
        }
        binding.btnSkip.setOnClickListener { viewModel.onGetStartedClicked() }
    }

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.viewPager.currentItem == 0) {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                binding.viewPager.currentItem -= 1
            }
        }
    }

    private fun observeNavigation() {
        observeFlow(viewModel.navigateToHome) {
            findNavController().navigate(R.id.action_onBoardingFragment_to_todoFragment)
        }
    }
}