package com.skyyo.template.features.thirdTab

import androidx.annotation.StringRes

sealed class ThirdTabEvent {
    class ShowLongToast(@StringRes val stringId: Int) : ThirdTabEvent()
}
