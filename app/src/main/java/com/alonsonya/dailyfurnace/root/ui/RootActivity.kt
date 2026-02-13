package com.alonsonya.dailyfurnace.root.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private val requestNotifications =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Log.d("FCM", "POST_NOTIFICATIONS granted=$granted")
        }

    private fun ensureNotificationsPermission() {
        if (Build.VERSION.SDK_INT < 33) return

        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            requestNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

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

        ensureNotificationsPermission()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { t ->
                Log.d("FCM", "TOKEN=$t")
            }
            .addOnFailureListener { e -> Log.e("FCM", "getToken failed", e) }

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                Log.d("FCM", "subscribe all ok=${task.isSuccessful}")
                task.exception?.let { Log.e("FCM", "subscribe all error", it) }
            }

        FirebaseMessaging.getInstance().subscribeToTopic("facts")
            .addOnCompleteListener { task ->
                Log.d("FCM", "subscribe facts ok=${task.isSuccessful}")
                task.exception?.let { Log.e("FCM", "subscribe facts error", it) }
            }

    }

}