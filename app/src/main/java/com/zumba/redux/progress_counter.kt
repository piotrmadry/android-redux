package com.zumba.redux

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.concurrent.atomic.AtomicInteger

class ProgressCounter {
    private val count = AtomicInteger()
    private val progressState = MutableStateFlow(count.get())
    
    val observeState: Flow<Boolean>
        get() = progressState.map { it > 0 }.distinctUntilChanged()
    
    fun addProgress() {
        progressState.value = count.incrementAndGet()
    }
    
    fun removeProgress() {
        progressState.value = count.decrementAndGet()
    }
}

fun <T> Flow<T>.watchProgress(counter: ProgressCounter): Flow<T> {
    return onStart { counter.addProgress() }
        .onCompletion { counter.removeProgress() }
}