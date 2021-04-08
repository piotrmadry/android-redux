package com.zumba.redux

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KProperty1

abstract class ReduxViewModel<State, Effects : ReduxEffects>(initialState: State) : ViewModel() {
    private val state = MutableStateFlow(initialState)
    private val effects = MutableSharedFlow<Effects>()
    private val stateMutex = Mutex()
    protected val progress = ProgressCounter()
    
    val currentState: LiveData<State>
        get() = state.asLiveData()
    
    val currentEffects: LiveData<Effects>
        get() = effects.asLiveData()
    
    protected suspend fun <T> Flow<T>.collectAndSetState(reducer: State.(T) -> State) {
        return collect { item -> setState { reducer(item) } }
    }
    
    fun <A> selectObserve(prop1: KProperty1<State, A>): Flow<A> {
        return selectSubscribe(prop1)
    }
    
    protected suspend fun setState(reducer: State.() -> State) {
        stateMutex.withLock {
            state.value = reducer(state.value)
        }
    }
    
    protected suspend fun setEffect(reducer: State.() -> Effects) {
        stateMutex.withLock {
            effects.emit(reducer(state.value))
        }
    }
    
    protected fun subscribe(block: (State) -> Unit) {
        viewModelScope.launch {
            state.collect { block(it) }
        }
    }
    
    protected fun <A> selectSubscribe(prop1: KProperty1<State, A>, block: (A) -> Unit) {
        viewModelScope.launch {
            selectSubscribe(prop1).collect { block(it) }
        }
    }
    
    private fun <A> selectSubscribe(prop1: KProperty1<State, A>): Flow<A> {
        return state.map { prop1.get(it) }.distinctUntilChanged()
    }
    
    protected fun CoroutineScope.launchSetState(reducer: State.() -> State) {
        launch { this@ReduxViewModel.setState(reducer) }
    }
    
    protected fun CoroutineScope.launchSetEffect(reducer: State.() -> Effects) {
        launch { this@ReduxViewModel.setEffect(reducer) }
    }
    
    protected suspend fun withState(block: (State) -> Unit) {
        stateMutex.withLock {
            block(state.value)
        }
    }
    
    protected fun CoroutineScope.withState(block: (State) -> Unit) {
        launch { this@ReduxViewModel.withState(block) }
    }
}
//:DD 