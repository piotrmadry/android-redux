package com.zumba.redux.viewmodel

import androidx.lifecycle.viewModelScope
import com.zumba.redux.base.*
import com.zumba.redux.usecase.FlowGetItemsUseCase
import com.zumba.redux.usecase.ViewItem
import kotlinx.coroutines.launch

data class ListState(
    val list: List<ViewItem> = listOf(),
    val error: BasicError? = null,
    val progress: Boolean = false
)

class ListViewModel(private val getItemsUseCase: FlowGetItemsUseCase) :
    ReduxViewModel<ListState, NoEffects>(
        ListState()
    ) {

    init {
        getData()

        viewModelScope.launch {
            progress.observeState
                .collectAndSetState { progress -> copy(progress = progress) }
        }
    }

    private fun getData() {
        viewModelScope.launch {
            getItemsUseCase(Unit)
                .watchProgressOnEach(progress)
                .collectAndSetState { result ->
                    result.fold(
                        { copy(error = it) },
                        { copy(list = it) })
                }
        }
    }

    fun refresh() = getData()
}
