package com.skyyo.template.features.secondTab

import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class SecondTabViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    val events = Channel<SecondTabEvent>(Channel.UNLIMITED)

    fun goSignIn() = navigationDispatcher.emit { it.navigate(R.id.goSignIn) }
}
