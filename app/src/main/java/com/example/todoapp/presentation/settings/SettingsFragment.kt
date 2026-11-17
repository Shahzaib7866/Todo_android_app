package com.example.todoapp.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.R
import com.example.todoapp.core.shared.BaseFragment
import com.example.todoapp.databinding.SettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsBinding, SettingsViewModel>(
    SettingsBinding::inflate,
    SettingsViewModel::class.java
) {

    override fun onViewReady() {
        // Sirf user ke dabane par theme badle
        binding.radioLight.setOnClickListener { applyTheme(AppCompatDelegate.MODE_NIGHT_NO) }
        binding.radioDark.setOnClickListener { applyTheme(AppCompatDelegate.MODE_NIGHT_YES) }
    }

    override fun observeViewModel() {
        // Saved theme ke mutabiq radio check karo
        viewModel.themeMode.observe(viewLifecycleOwner) { mode ->
            binding.radioGroupTheme.check(
                if (mode == AppCompatDelegate.MODE_NIGHT_YES) R.id.radioDark else R.id.radioLight
            )
        }
    }

    private fun applyTheme(mode: Int) {
        viewModel.setThemeMode(mode)                    // DataStore mein save
        AppCompatDelegate.setDefaultNightMode(mode)     // abhi lagao (Activity dobara banti hai)
    }
}