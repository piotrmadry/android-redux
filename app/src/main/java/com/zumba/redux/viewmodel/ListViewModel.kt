package com.zumba.redux.viewmodel

import androidx.lifecycle.viewModelScope
import com.zumba.redux.base.BasicError
import com.zumba.redux.base.NoEffects
import com.zumba.redux.base.ReduxViewModel
import com.zumba.redux.usecase.FlowGetItemsUseCase
import com.zumba.redux.usecase.ViewItem
import kotlinx.coroutines.launch

data class ListState(
    val list: List<ViewItem> = listOf(),
    val error: BasicError? = null
)

class ListViewModel(private val getItemsUseCase: FlowGetItemsUseCase) :
    ReduxViewModel<ListState, NoEffects>(
        ListState()
    ) {

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            getItemsUseCase(Unit)
                .collectAndSetState { result ->
                    result.fold(
                        { copy(error = it) },
                        { copy(list = it) })
                }
        }
    }

    fun refresh() = getData()
}
