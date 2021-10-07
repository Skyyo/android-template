package com.skyyo.template.features.signIn

import androidx.annotation.StringRes

sealed class SignInEvent {
    class ShowLongToast(@StringRes val stringId: Int) : SignInEvent()
}
