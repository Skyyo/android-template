package com.skyyo.template.features.signIn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skyyo.template.R
import com.skyyo.template.application.models.local.InputWrapper
import com.skyyo.template.application.models.remote.SocialSignInRequest
import com.skyyo.template.application.repositories.auth.AuthRepository
import com.skyyo.template.application.repositories.auth.SocialSignInResult
import com.skyyo.template.utils.InputValidator
import com.skyyo.template.utils.eventDispatchers.NavigationDispatcher
import com.skyyo.template.utils.extensions.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = handle.getStateFlow(viewModelScope, "email", InputWrapper())
    val password = handle.getStateFlow(viewModelScope, "password", InputWrapper())
    val isPasswordVisible = handle.getStateFlow(viewModelScope, "isPasswordVisible", false)
    var stateRelatedVariable = false

    val areInputsValid = combine(email, password) { email, password ->
        email.errorId == null && email.value.isNotEmpty() && password.errorId == null && password.value.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val events = Channel<SignInEvent>(Channel.UNLIMITED)

    fun onEmailEntered(input: String) {
        stateRelatedVariable = true
        val error = InputValidator.getEmailErrorIdOrNull(input)
        email.value = email.value.copy(value = input, errorId = error)
    }

    fun onPasswordEntered(input: String) {
        val error = InputValidator.getPasswordErrorIdOrNull(input)
        password.value = password.value.copy(value = input, errorId = error)
    }

    fun onPasswordVisibilityClick() {
        isPasswordVisible.value = !isPasswordVisible.value
    }

    fun goHome() = navigationDispatcher.emit { it.navigate(R.id.goHome) }

    private fun authGoogle(googleToken: String) = viewModelScope.launch(Dispatchers.IO) {
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
