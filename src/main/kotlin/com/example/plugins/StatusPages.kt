package com.example.plugins

import com.example.exceptions.ConflictException
import com.example.exceptions.InvalidInputException
import com.example.exceptions.ResourceNotFoundException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Application.configureStatusPages(){
    install(StatusPages){
        exception<Throwable> { call, cause ->
            when(cause){
                is InvalidInputException -> call.respond(status = HttpStatusCode.BadRequest, message = cause.body)
                is ResourceNotFoundException -> call.respond(status = HttpStatusCode.NotFound, message = cause.body)
                is ExposedSQLException -> call.respond(status = HttpStatusCode.Conflict, message = ConflictException(call, cause).body)
                else -> call.respondText(text = "500: ${cause.message}", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}