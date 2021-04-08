package com.zumba.redux

import RemoveItemUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FinishActivity : ReduxEffects

class DetailsViewModel(
    private val id: Int,
    private val useCase: RemoveItemUseCase
) : ReduxViewModel<ListState, FinishActivity>(ListState()) {

    fun remove() {
        viewModelScope.launch {
            useCase(id)
                .collect { setEffect { FinishActivity() } }
        }
    }

}