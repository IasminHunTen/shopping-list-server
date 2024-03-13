package com.example.dao

import com.example.models.Items
import com.example.models.ShoppingLists
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init(config: ApplicationConfig){
        val database = Database.connect(
            url = config.property("storage.jdbcURL").getString(),
            driver = config.property("storage.driverClassName").getString()
        )
        transaction(database) {
            SchemaUtils.create(ShoppingLists, Items)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}