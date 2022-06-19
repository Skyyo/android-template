package com.skyyo.template.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }

const val SUBSCRIPTION_TIMEOUT = 5000L

fun <T> Flow<T>.asStateFlow(
    scope: CoroutineScope,
    initialValue: T,
    context: CoroutineContext = Dispatchers.Default,
    started: SharingStarted = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
): StateFlow<T> = flowOn(context).stateIn(scope, started, initialValue)

fun <T> LiveData<T>.asStateFlow(
    scope: CoroutineScope,
    initialValue: T,
    context: CoroutineContext = Dispatchers.Default,
    started: SharingStarted = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
): StateFlow<T> = asFlow().flowOn(context).stateIn(scope, started, initialValue)
