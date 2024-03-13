package com.example.models

import com.example.exceptions.InvalidInputException
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class ShoppingList(
    val id: Int? = null,
    val title: String,
    val completedItems: Int = 0,
    val allItems: Int = 0
){
    object Boundaries {
        const val TITLE_LENGTH = 128
    }
    object Constrains {
        const val UNIQUE_TITLE = "A shopping list with this title already exists"
    }
}

fun ShoppingList.validateInput(){
    if(title.isBlank())
        throw InvalidInputException("Title must not be blank", "title")
    if(title.length > ShoppingList.Boundaries.TITLE_LENGTH)
        throw InvalidInputException("The shopping list title must not exceed ${ShoppingList.Boundaries.TITLE_LENGTH} length", "title")
}

object ShoppingLists : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", length = ShoppingList.Boundaries.TITLE_LENGTH).uniqueIndex()
    val completedItems = integer("completed_items")
    val allItems = integer("all_items")

    override val primaryKey = PrimaryKey(id)
}
