package com.fady.starwars.platform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.Job

/**
 * Utility functions to bridge Kotlin StateFlow to iOS
 * This provides a way for iOS to observe Kotlin StateFlow emissions
 */

/**
 * Creates a closeable subscription to a StateFlow that can be used from iOS
 * This function is specifically designed to work with KMP iOS bridging
 */
fun <T : Any> StateFlow<T>.subscribe(
    onEach: (T) -> Unit
): Closeable {
    val scope = CoroutineScope(Dispatchers.Main)
    val job = this
        .onEach { value -> onEach(value) }
        .launchIn(scope)
    
    return object : Closeable {
        override fun close() {
            job.cancel()
        }
    }
}

/**
 * Closeable interface for iOS to cancel subscriptions
 */
interface Closeable {
    fun close()
}