package com.skyyo.template.features.home

import androidx.annotation.StringRes

sealed class HomeEvent

class ShowLongToast(@StringRes val stringId: Int) : HomeEvent()
class UpdateProgress(val showProgress: Boolean) : HomeEvent()
