package com.example.kotlinshowcase.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

/**
 * Collects the latest value from a Flow in a lifecycle-aware manner.
 * This is useful for operations that should only process the most recent value.
 *
 * @param lifecycle The lifecycle to follow for flow collection
 * @param minActiveState The minimum lifecycle state in which collection should occur
 * @param action The action to perform on each collected value
 */
@Composable
fun <T> Flow<T>.collectLatestWithLifecycle(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit
) {
    val flow = remember(this, lifecycle) {
        this.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = minActiveState
        )
    }
    
    LaunchedEffect(flow) {
        flow.collectLatest(action)
    }
}

/**
 * Collects values from a Flow in a lifecycle-aware manner and returns them as State.
 * The State will be updated whenever the Flow emits a new value.
 * For StateFlow, consider using collectAsState() directly for better performance.
 *
 * @param lifecycle The lifecycle to follow for flow collection
 * @param minActiveState The minimum lifecycle state in which collection should occur
 * @param initial The initial value to use for the State
 * @return A State object holding the current value from the Flow
 */
@Composable
fun <T> Flow<T>.collectAsStateWithLifecycle(
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    initial: T? = null
): State<T?> {
    val flow = remember(this, lifecycle) {
        this.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = minActiveState
        )
    }
    
    return flow.collectAsState(initial = initial)
}

/**
 * Convenience extension for StateFlow that delegates to collectAsState().
 * This is the recommended way to collect StateFlow values in Compose.
 */
@Composable
fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> = collectAsState()
