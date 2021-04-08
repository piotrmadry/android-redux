package com.zumba.redux.usecase

import com.zumba.redux.base.FlowUseCase
import com.zumba.redux.base.ResultUseCase
import com.zumba.redux.base.UseCase
import com.zumba.redux.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ViewItem(val id: Int, val name: String)

class GetItemsUseCase(private val repository: Repository) : ResultUseCase<Unit, List<ViewItem>>() {
    override suspend fun doWork(parameters: Unit): List<ViewItem> =
        repository.getItems().map { item -> ViewItem(id = item.id, name = item.name) }
}

class RemoveItemUseCase(private val repository: Repository) : UseCase<Int>() {
    override suspend fun doWork(parameters: Int) = repository.removeItem(parameters)
}

class FlowGetItemsUseCase(private val repository: Repository) :
    FlowUseCase<Unit, List<ViewItem>>() {
    override fun execute(parameters: Unit): Flow<List<ViewItem>> {
        return repository.getListFlow()
            .map { list -> list.map { item -> ViewItem(id = item.id, name = item.name) } }
    }
}