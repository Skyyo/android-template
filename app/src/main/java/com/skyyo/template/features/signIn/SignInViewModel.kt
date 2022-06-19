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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val EMAIL = "email"
const val PASSWORD = "password"
const val IS_PASSWORD_VISIBLE = "isPasswordVisible"

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = handle.getStateFlow(EMAIL, InputWrapper())
    val password = handle.getStateFlow(PASSWORD, InputWrapper())
    val isPasswordVisible = handle.getStateFlow(IS_PASSWORD_VISIBLE, false)
    var stateRelatedVariable = false

    val areInputsValid = combine(email, password) { email, password ->
        email.errorId == null && email.value.isNotEmpty() && password.errorId == null && password.value.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val events = Channel<SignInEvent>(Channel.UNLIMITED)

    fun onEmailEntered(input: String) {
        stateRelatedVariable = true
        val error = InputValidator.getEmailErrorIdOrNull(input)
        handle[EMAIL] = email.value.copy(value = input, errorId = error)
    }

    fun onPasswordEntered(input: String) {
        val error = InputValidator.getPasswordErrorIdOrNull(input)
        handle[PASSWORD] = password.value.copy(value = input, errorId = error)
    }

    fun onPasswordVisibilityClick() {
        handle[IS_PASSWORD_VISIBLE] = !isPasswordVisible.value
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
