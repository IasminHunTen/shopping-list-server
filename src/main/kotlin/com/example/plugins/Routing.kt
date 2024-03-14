package com.example.plugins

import com.example.models.Dummies
import com.example.routes.itemsRoutes
import com.example.routes.shoppingListRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("Welcome to shopping list rest API")
        }
        get("/dummy/{flag?}"){
            val flag = call.parameters["flag"]?.toIntOrNull() ?: 0
            if(flag == 0)
                call.respond(Dummies.Bad("an error message"))
            else
                call.respond(Dummies.Good(10))

        }
        shoppingListRoutes()
        itemsRoutes()
    }
}
