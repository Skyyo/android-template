package com.skyyo.template.features.home

import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    val events = Channel<HomeEvent>(Channel.UNLIMITED)

    fun goSignIn() = navigationDispatcher.emit { it.navigate(R.id.goSignIn) }
}
