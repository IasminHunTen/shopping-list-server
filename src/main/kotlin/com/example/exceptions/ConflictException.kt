package com.example.exceptions

import com.example.models.Item
import com.example.models.ShoppingList
import com.example.routes.RoutesPaths
import io.ktor.server.application.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

class ConflictException(private val call: ApplicationCall, private val e: ExposedSQLException) : Throwable() {
    var body = InvalidInputResponse("")

    init {
        body = when(e.errorCode){
            FOREIGN_KEY_CONSTRAINT -> InvalidInputResponse(Item.Constrains.FK_DOES_NOT_EXIST)
            UNIQUE_CONSTRAINT -> with(call.request.local.uri){
                when {
                    contains(RoutesPaths.SHOPPING_LIST.path) ->
                        InvalidInputResponse(ShoppingList.Constrains.UNIQUE_TITLE, "title")
                    contains(RoutesPaths.ITEM.path) ->
                        InvalidInputResponse(Item.Constrains.UNIQUE_NAME, "name")
                    else -> body
                }
            }
            else -> body
        }
    }

    private companion object ViolationCodes {
        const val UNIQUE_CONSTRAINT = 23505
        const val FOREIGN_KEY_CONSTRAINT = 23506
    }
}