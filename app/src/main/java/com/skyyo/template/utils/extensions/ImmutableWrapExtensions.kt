@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.skyyo.template.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Workaround for passing Collections to Composables to render them stable.
 * Can also be used for 3d party non-stability inferred objects
 */
@ExperimentalLifecycleComposeApi
@Composable
fun <T> Flow<T>.collectAsStateWithLifecycleImmutable(
    initialValue: ImmutableWrap<T>,
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<ImmutableWrap<T>> {
    return produceState(initialValue, this, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@collectAsStateWithLifecycleImmutable.collect {
                    this@produceState.value = ImmutableWrap(it)
                }
            } else withContext(context) {
                this@collectAsStateWithLifecycleImmutable.collect {
                    this@produceState.value = ImmutableWrap(it)
                }
            }
        }
    }
}

/**
 * Workaround for passing Collections to Composables to render them stable.
 * Can also be used for 3d party non-stability inferred objects
 */
@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycleImmutable(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<ImmutableWrap<T>> = collectAsStateWithLifecycleImmutable(
    initialValue = ImmutableWrap(value),
    lifecycle = lifecycleOwner.lifecycle,
    minActiveState = minActiveState,
    context = context
)

/**
 * Can be used to wrap collections or 3d party non-stability inferred objects
 */
@Immutable
class ImmutableWrap<T>(val value: T)
