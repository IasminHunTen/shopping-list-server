package com.example.routes

import com.example.dao.itemsDao
import com.example.exceptions.InvalidInputException
import com.example.exceptions.ResourceNotFoundException
import com.example.models.Item
import com.example.models.TextResponse
import com.example.models.validateInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.itemsRoutes() {
    route(RoutesPaths.ITEM.path) {

        get("{id?}") {
            val id = call.parameters["id"]?.toInt() ?: throw InvalidInputException("Missing id from path")
            call.respond(status = HttpStatusCode.OK, itemsDao.getShoppingListItems(id))
        }

        post {
            val input = call.receive<Item>()
            input.validateInput()
            call.respond(status = HttpStatusCode.Created, message = itemsDao.newItem(input))
        }

        put {
            val input = call.receive<Item>()
            input.validateInput()
            if(itemsDao.updateItem(input))
                call.respond(status = HttpStatusCode.NoContent, message = TextResponse())
            else
                throw ResourceNotFoundException("No item with '${input.name}' name, has been found")
        }

        delete("{id?}") {
            val id = call.parameters["id"]?.toInt() ?: throw InvalidInputException("Missing id from path")
            if(itemsDao.deleteItem(id))
                call.respond(status = HttpStatusCode.Accepted, message = TextResponse("item removed"))
            else
                throw ResourceNotFoundException("No item with id($id), has been found")
        }

        put("state") {
            val input = call.receive<Item>()
            if (itemsDao.updateItemState(input))
                call.respond(status = HttpStatusCode.NoContent, message = TextResponse())
            else
                throw ResourceNotFoundException("No item with '${input.name}' name, has been found")
        }
    }
}