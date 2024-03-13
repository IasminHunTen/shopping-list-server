package com.example.models

import com.example.exceptions.InvalidInputException
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Item(
    val id: Int? = null,
    val shoppingListId: Int,
    val name: String,
    val quantity: Int,
    val completed: Boolean = false
){
    object Boundaries {
        const val NAME_LENGTH = 128
    }
    object Constrains {
        const val UNIQUE_NAME = "An item with this name already exists"
        const val FK_DOES_NOT_EXIST = "There is no shopping list with the provided id"
    }
}

fun Item.validateInput(){
    if(name.isBlank())
        throw InvalidInputException("The item name must not be blank", "name")
    if(name.length > Item.Boundaries.NAME_LENGTH)
        throw InvalidInputException("The item name must not exceed ${Item.Boundaries.NAME_LENGTH} length", "name")
    if(quantity < 1)
        throw InvalidInputException("The quantity must be represented with a positive integer", "quantity")
}

object Items : Table() {
    val id = integer("id").autoIncrement()
    val shoppingListId = reference("shopping_list_id", refColumn = ShoppingLists.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", length = Item.Boundaries.NAME_LENGTH)
    val quantity = integer("quantity")
    val completed = bool("completed")

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(shoppingListId, name)
    }
}
