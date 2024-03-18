package com.example.plugins

import com.example.dao.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabases() {
    DatabaseSingleton.init(environment.config)
}
