package com.example.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.todoapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var bottomBarVisible = false

    private val topLevelDestinations = setOf(
        R.id.homeFragment, R.id.calendarFragment, R.id.statusFragment, R.id.settingsFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, if (bottomBarVisible) 0 else bars.bottom)
            insets
        }
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId, null, navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(R.id.homeFragment) { saveState = true }
            })
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomBarVisible = destination.id in topLevelDestinations
            binding.bottomNavigation.isVisible = bottomBarVisible
            binding.bottomNavigation.menu.findItem(destination.id)?.isChecked = true
            ViewCompat.requestApplyInsets(binding.main)
        }
    }
}