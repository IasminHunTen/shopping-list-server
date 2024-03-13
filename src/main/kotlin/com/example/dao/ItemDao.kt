package com.example.dao

import com.example.models.Item

interface ItemDao {
    suspend fun getShoppingListItems(shoppingListId: Int): List<Item>
    suspend fun newItem(item: Item): Item
    suspend fun updateItem(item: Item): Boolean
    suspend fun updateItemState(item: Item): Boolean
    suspend fun deleteItem(itemId: Int): Boolean
}