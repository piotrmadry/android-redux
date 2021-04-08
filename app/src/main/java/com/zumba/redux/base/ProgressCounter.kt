package com.zumba.redux.base

import kotlinx.coroutines.flow.*
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
        if(count.get() > 0){
            progressState.value = count.decrementAndGet()
        }
    }
}

fun <T> Flow<T>.watchProgressOnCompletion(counter: ProgressCounter): Flow<T> {
    return onStart { counter.addProgress() }
        .onCompletion { counter.removeProgress() }
}

fun <T> Flow<T>.watchProgressOnEach(counter: ProgressCounter): Flow<T> {
    return onStart { counter.addProgress() }
        .onEach { counter.removeProgress() }
}