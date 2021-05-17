package com.skyyo.template.features.auth.signIn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.template.R
import com.skyyo.template.application.models.remote.SocialSignInRequest
import com.skyyo.template.application.repositories.auth.AuthRepository
import com.skyyo.template.application.repositories.auth.SocialSignInResult
import com.skyyo.template.extensions.isEmail
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    val events = Channel<SignInEvent>()
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
            events.trySend(EmailValidationError(stringId = null))
            true
        }
        else -> {
            events.trySend(EmailValidationError(stringId = R.string.app_name))
            false
        }
    }

    private fun goHome() = navigationDispatcher.emit { it.navigate(R.id.goHome) }

    fun authGoogle(googleToken: String) = viewModelScope.launch(Dispatchers.IO) {
        when (authRepository.authGoogle(SocialSignInRequest(idToken = googleToken))) {
            SocialSignInResult.SuccessFirstTime -> {
            }
            SocialSignInResult.Success -> {
            } // navigate somewhere etc
            SocialSignInResult.NetworkError -> {
            } // events.send(error)
        }
    }
}
