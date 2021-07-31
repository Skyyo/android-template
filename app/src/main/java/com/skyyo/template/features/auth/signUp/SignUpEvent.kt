package com.skyyo.template.features.auth.signUp

import androidx.annotation.StringRes

sealed class SignUpEvent {
    class ShowLongToast(@StringRes val stringId: Int) : SignUpEvent()
}
