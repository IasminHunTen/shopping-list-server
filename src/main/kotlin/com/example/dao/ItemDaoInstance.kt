package com.example.dao

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.exceptions.InvalidInputException
import com.example.exceptions.ResourceNotFoundException
import com.example.models.Item
import com.example.models.Items
import com.example.models.ShoppingLists
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ItemDaoInstance : ItemDao {
    override suspend fun getShoppingListItems(shoppingListId: Int): List<Item> = dbQuery {
        ShoppingLists.select { ShoppingLists.id eq shoppingListId }.singleOrNull().let{
            if (it == null){
                throw ResourceNotFoundException("There is no shopping list with id: $shoppingListId")
            }
        }
        Items.select { Items.shoppingListId eq shoppingListId }.map(::resultRowToItem)
    }

    override suspend fun newItem(item: Item): Item = dbQuery {
        val insertStatement = Items.insert {
            it[shoppingListId] = item.shoppingListId
            it[name] = item.name
            it[quantity] = item.quantity
            it[completed] = item.completed
        }
        insertStatement.resultedValues!!.single().let(::resultRowToItem).also {
            ShoppingLists.update({ ShoppingLists.id eq item.shoppingListId }){
                with(SqlExpressionBuilder){
                    it.update(allItems, allItems + 1)
                }
            }
        }
    }

    override suspend fun updateItem(item: Item): Boolean = dbQuery {
        Items.update({ Items.id eq item.id!! }) {
            it[name] = item.name
            it[quantity] = item.quantity
        } > 0
    }

    override suspend fun updateItemState(item: Item): Boolean = dbQuery {
        val result = Items.update({ Items.id eq item.id!! }) {
            it[completed] = !item.completed
        } > 0
        result.also {
            ShoppingLists.update({ ShoppingLists.id eq item.shoppingListId }) {
                with(SqlExpressionBuilder) {
                    it.update(completedItems, if(item.completed) (completedItems - 1) else (completedItems + 1))
                }
            }
        }
    }

    override suspend fun deleteItem(itemId: Int): Boolean = dbQuery {
        Items.deleteWhere { id eq itemId } > 0
    }

    private fun resultRowToItem(row: ResultRow) = Item(
        id = row[Items.id],
        shoppingListId = row[Items.shoppingListId],
        name = row[Items.name],
        quantity = row[Items.quantity],
        completed = row[Items.completed]
    )
}

val itemsDao = ItemDaoInstance()