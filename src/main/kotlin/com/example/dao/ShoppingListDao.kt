package com.example.dao

import com.example.models.ShoppingList

interface ShoppingListDao {
    suspend fun all(): List<ShoppingList>
    suspend fun newShoppingList(shoppingList: ShoppingList): ShoppingList
    suspend fun deleteShoppingList(shoppingListId: Int): Boolean
}