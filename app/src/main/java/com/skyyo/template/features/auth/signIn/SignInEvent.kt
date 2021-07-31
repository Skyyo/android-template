package com.skyyo.template.features.auth.signIn

import androidx.annotation.StringRes

sealed class SignInEvent {
    class ShowLongToast(@StringRes val stringId: Int) : SignInEvent()
    class UpdateProgress(val showProgress: Boolean) : SignInEvent()
    class EmailValidationError(@StringRes val stringId: Int?) : SignInEvent()
}
