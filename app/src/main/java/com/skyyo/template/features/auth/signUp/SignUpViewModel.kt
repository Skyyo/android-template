package com.skyyo.template.features.auth.signUp

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import kotlinx.coroutines.channels.Channel

class SignUpViewModel @ViewModelInject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    val events = Channel<SignUpEvent>(Channel.UNLIMITED)
    val state = handle.getLiveData("state", SignUpFragment.stateX)

    private fun goHome() = navigationDispatcher.emit { it.navigate(R.id.goHome) }
}
