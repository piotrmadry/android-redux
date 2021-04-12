package com.zumba.redux.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

data class Item(val id: Int, val name: String)

object Repository {
    private val refresh = MutableSharedFlow<Unit>()
    private val items: MutableList<Item> =
        (0..99).map { iterator -> Item(id = iterator, name = "Name $iterator") }.toMutableList()

    suspend fun getItems() =
        items.map { it.copy(name = "${it.name} ${System.currentTimeMillis()}") }

    fun getListFlow(): Flow<List<Item>> = refresh.onStart { emit(Unit) }.flatMapLatest {
        flow {
            delay(2000)
            emit(getItems())
        }
    }


    suspend fun removeItem(id: Int) {
        delay(1000)
        items.removeAll { item -> item.id == id }
        refresh.emit(Unit)
    }
}
