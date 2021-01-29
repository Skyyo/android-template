package com.skyyo.template.features.auth.signUp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle
) : ViewModel() {

    val events = Channel<SignUpEvent>(Channel.UNLIMITED)
    val state = handle.getLiveData("state", SignUpFragment.stateX)

    private fun goHome() = navigationDispatcher.emit { it.navigate(R.id.goHome) }
}
