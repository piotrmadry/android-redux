package com.zumba.redux

import GetItemsUseCase
import ViewItem
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class ListState(
    val list: List<ViewItem> = listOf(),
    val error: BasicError? = null
)

class ListViewModel(private val getItemsUseCase: GetItemsUseCase) : ReduxViewModel<ListState, NoEffects>(ListState()) {
    
    init {
       getData()

    }
    
    private fun getData() {
        viewModelScope.launch {
            getItemsUseCase()
                .collectAndSetState { result ->  result.fold({ copy(error = it) }, { copy(list = it)})}
        }
    }
    
    fun refresh() = getData()
}
