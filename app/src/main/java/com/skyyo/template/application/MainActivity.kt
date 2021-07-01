package com.skyyo.template.application

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.skyyo.template.R
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.databinding.ActivityMainBinding
import com.skyyo.template.extensions.changeSystemBars
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

typealias onDestinationChanged = NavController.OnDestinationChangedListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val destinationChangedListener = onDestinationChanged { _, destination, _ ->
        when (destination.id) {
            R.id.fragmentSignIn,
            R.id.fragmentSignUp -> {
                binding.fragmentHost.changeSystemBars(light = false)
                updateBottomNavigationView(visible = false)
            }
            else -> {
                binding.fragmentHost.changeSystemBars(light = true)
                updateBottomNavigationView(visible = true)
            }
        }
    }

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    @Inject
    lateinit var unauthorizedEventDispatcher: UnauthorizedEventDispatcher

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockIntoPortrait()
        setTheme(R.style.ThemeDay)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge()
        initNavigation()
        lifecycleScope.launchWhenResumed { observeUnauthorizedEvent() }
        lifecycleScope.launchWhenResumed { observeNavigationCommands() }
    }

    private fun initNavigation() {
        (supportFragmentManager.findFragmentById(R.id.fragmentHost) as NavHostFragment).also { navHost ->
            val navInflater = navHost.navController.navInflater
            val navGraph = navInflater.inflate(R.navigation.navigation_graph).apply {
                setStartDestination(startDestination())
            }
            navHost.navController.graph = navGraph
            navController = navHost.navController
            navController.addOnDestinationChangedListener(destinationChangedListener)
            binding.bnv.setupWithNavController(navController)
        }
    }

    private fun startDestination(): Int {
        val accessToken = runBlocking { dataStoreManager.getAccessToken() }
        return if (accessToken == null) R.id.fragmentSignIn else R.id.fragmentHome
    }

    private fun updateBottomNavigationView(visible: Boolean) {
        // animate as slide or smth
        binding.bnv.isVisible = visible
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockIntoPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private suspend fun observeUnauthorizedEvent() {
        for (command in unauthorizedEventDispatcher.unauthorizedEventEmitter) {
            if (isFinishing) return
            @Suppress("GlobalCoroutineUsage")
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch(Dispatchers.IO) {
                dataStoreManager.clearData()
            }
            finish()
            startActivity(intent)
        }
    }

    private suspend fun observeNavigationCommands() {
        for (command in navigationDispatcher.navigationEmitter) {
            command.invoke(Navigation.findNavController(this@MainActivity, R.id.fragmentHost))
        }
    }

    fun returnToTopOfRootStack() {
        binding.bnv.selectedItemId = R.id.fragmentHome
    }
}
