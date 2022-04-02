package com.skyyo.template.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }

// don't use unless the difference between liveData & stateFlow for
// scenarios with search/flatMapLatest is used
fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    val observer = Observer<T> { value -> if (value != stateFlow.value) stateFlow.value = value }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) liveData.value = value
        }
    }.launchIn(scope)

    return stateFlow
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
