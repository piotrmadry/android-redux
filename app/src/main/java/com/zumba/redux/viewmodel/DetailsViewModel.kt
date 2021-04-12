package com.zumba.redux.viewmodel

import androidx.lifecycle.viewModelScope
import com.zumba.redux.base.ReduxEffects
import com.zumba.redux.base.ReduxViewModel
import com.zumba.redux.usecase.RemoveItemUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

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