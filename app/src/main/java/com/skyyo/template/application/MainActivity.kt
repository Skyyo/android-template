package com.skyyo.template.application

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.skyyo.template.R
import com.skyyo.template.application.persistance.DataStoreManager
import com.skyyo.template.databinding.ActivityMainBinding
import com.skyyo.template.extensions.changeSystemBars
import com.skyyo.template.extensions.setupWithNavController
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import com.skyyo.template.utils.eventDispatchers.UnauthorizedEventDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

typealias onDestinationChanged = NavController.OnDestinationChangedListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null
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
        if (savedInstanceState == null) initNavigation(restore = false)
        lifecycleScope.launchWhenResumed { observeUnauthorizedEvent() }
        lifecycleScope.launchWhenResumed { observeNavigationCommands() }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initNavigation(restore = true)
    }

    private fun initNavigation(restore: Boolean) {
        binding.bnv.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.home_graph,
                R.navigation.second_tab_graph,
                R.navigation.third_tab_graph,
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragmentHost,
            intent = intent
        ).also { controller ->
            if (!restore) startDestination()?.let { id -> controller.value?.navigate(id) }
            controller.observe(this) { navController ->
                with(navController) {
                    removeOnDestinationChangedListener(destinationChangedListener)
                    addOnDestinationChangedListener(destinationChangedListener)
                }
            }
            currentNavController = controller
        }
    }

    private fun startDestination(): Int? {
        val accessToken = runBlocking { dataStoreManager.getAccessToken() }
        return if (accessToken == null) R.id.goSignIn else null
    }

    private fun updateBottomNavigationView(visible: Boolean) {
        // animate as slide or smth
        binding.bnv.isVisible = visible
    }

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
}
