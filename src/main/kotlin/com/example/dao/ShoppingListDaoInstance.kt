package com.example.dao

import com.example.dao.DatabaseSingleton.dbQuery
import com.example.models.ShoppingList
import com.example.models.ShoppingLists
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class ShoppingListDaoInstance : ShoppingListDao {
    override suspend fun all(): List<ShoppingList> = dbQuery {
        ShoppingLists.selectAll().map(::resultRowToShoppingList)
    }

    override suspend fun newShoppingList(shoppingList: ShoppingList): ShoppingList = dbQuery {
        val insertStatement = ShoppingLists.insert {
            it[title] = shoppingList.title
            it[completedItems] = shoppingList.completedItems
            it[allItems] = shoppingList.allItems
        }
        insertStatement.resultedValues!!.single().let(::resultRowToShoppingList)
    }

    override suspend fun deleteShoppingList(shoppingListId: Int): Boolean = dbQuery {
        ShoppingLists.deleteWhere { ShoppingLists.id eq shoppingListId } > 0
    }

    private fun resultRowToShoppingList(row: ResultRow) = ShoppingList(
        id = row[ShoppingLists.id],
        title = row[ShoppingLists.title],
        completedItems = row[ShoppingLists.completedItems],
        allItems = row[ShoppingLists.allItems]
    )
}

val shoppingListDao = ShoppingListDaoInstance()