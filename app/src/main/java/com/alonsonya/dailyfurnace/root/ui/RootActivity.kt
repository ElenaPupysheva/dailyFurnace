package com.alonsonya.dailyfurnace.root.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alonsonya.dailyfurnace.R
import com.alonsonya.dailyfurnace.databinding.ActivityRootBinding
import com.google.firebase.messaging.FirebaseMessaging

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val initialContainerTop = binding.rootContainer.paddingTop
        val initialBottomNavBottom = binding.bottomNavigation.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.rootContainer) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updatePadding(top = initialContainerTop + bars.top)

            binding.bottomNavigation.updatePadding(bottom = initialBottomNavBottom + bars.bottom)

            insets
        }

        ViewCompat.requestApplyInsets(binding.rootContainer)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.furnaceFragment,
                R.id.favoriteFragment,
                R.id.collectionFragment,
                R.id.settingsFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { t ->
                Log.d("FCM", "TOKEN=$t")
            }
            .addOnFailureListener { e -> Log.e("FCM", "getToken failed", e) }
    }
}