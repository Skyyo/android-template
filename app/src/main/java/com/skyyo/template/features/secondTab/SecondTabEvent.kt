package com.skyyo.template.features.secondTab

import androidx.annotation.StringRes

sealed class SecondTabEvent {
    class ShowLongToast(@StringRes val stringId: Int) : SecondTabEvent()
}
