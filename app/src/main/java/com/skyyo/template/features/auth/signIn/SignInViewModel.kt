package com.skyyo.template.features.auth.signIn

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.template.R
import com.skyyo.template.application.models.remote.SocialSignInRequest
import com.skyyo.template.application.repositories.auth.AuthRepository
import com.skyyo.template.application.repositories.auth.SocialSignInNetworkError
import com.skyyo.template.application.repositories.auth.SocialSignInSuccess
import com.skyyo.template.application.repositories.auth.SocialSignInSuccessFirstTime
import com.skyyo.template.extensions.isEmail
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class SignInViewModel @ViewModelInject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    @Assisted private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    val events = Channel<SignInEvent>(Channel.UNLIMITED)
    var email = handle.get<String>("email")
        set(value) {
            field = value
            handle.set("email", field)
        }

    fun onEmailEntered(input: String) {
        email = input
        isEmailValid()
    }

    private fun isEmailValid() = when {
        email.isEmail -> {
            events.offer(EmailValidationError(stringId = null))
            true
        }
        else -> {
            events.offer(EmailValidationError(stringId = R.string.app_name))
            false
        }
    }

    private fun goHome() = navigationDispatcher.emit { it.navigate(R.id.goHome) }

    fun authGoogle(googleToken: String) = viewModelScope.launch(Dispatchers.IO) {
        when (authRepository.authGoogle(SocialSignInRequest(idToken = googleToken))) {
            SocialSignInSuccessFirstTime -> {
            }
            SocialSignInSuccess -> {
            } // navigate somewhere etc
            SocialSignInNetworkError -> {
            } // events.send(error)
        }
    }
}
