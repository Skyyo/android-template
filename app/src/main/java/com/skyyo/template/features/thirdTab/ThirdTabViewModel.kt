package com.skyyo.template.features.thirdTab

import androidx.lifecycle.ViewModel
import com.skyyo.template.R
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class ThirdTabViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
) : ViewModel() {

    val events = Channel<ThirdTabEvent>(Channel.UNLIMITED)

    fun goSignIn() = navigationDispatcher.emit { it.navigate(R.id.goSignIn) }
}
