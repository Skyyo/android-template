package com.skyyo.template.features.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import kotlinx.coroutines.channels.Channel

class HomeViewModel @ViewModelInject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    val events = Channel<HomeEvent>(Channel.UNLIMITED)

    fun goSignIn() = navigationDispatcher.emit { it.navigate(R.id.goSignIn) }
}
