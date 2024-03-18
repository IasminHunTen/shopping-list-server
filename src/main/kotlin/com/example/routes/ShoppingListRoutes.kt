package com.example.routes

import com.example.dao.shoppingListDao
import com.example.exceptions.InvalidInputException
import com.example.exceptions.ResourceNotFoundException
import com.example.models.ShoppingList
import com.example.models.TextResponse
import com.example.models.validateInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.shoppingListRoutes() {
    route(RoutesPaths.SHOPPING_LIST.path) {

        get {
            call.respond(status = HttpStatusCode.OK, shoppingListDao.all())
        }

        post {
            val input = call.receive<ShoppingList>()
            input.validateInput()
            call.respond(status = HttpStatusCode.Created, message = shoppingListDao.newShoppingList(input))
        }

        delete("{id?}") {
            val id = call.parameters["id"]?.toInt() ?: throw InvalidInputException("Missing id from path")
            if(shoppingListDao.deleteShoppingList(id))
                call.respond(status = HttpStatusCode.Accepted, message = TextResponse("shopping list removed"))
            else
                throw ResourceNotFoundException("No shopping list with id($id), has been found")
        }
    }
}