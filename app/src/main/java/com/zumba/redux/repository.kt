package com.zumba.redux

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class Item(val id: Int, val name: String)

class Repository {
    private val items: MutableList<Item> = (0..99).map { iterator -> Item(id = iterator, name = "Name $iterator") }.toMutableList()
    
    suspend fun getItems() = items.map { it.copy(name = "${it.name} ${System.currentTimeMillis()}") }
    suspend fun getList(): List<Item> = items
    
    fun getListFlow(): Flow<List<Item>> = flow {emit(items)}
    
    suspend fun removeItem(item: Item) {
        items.remove(item)
    }
}
