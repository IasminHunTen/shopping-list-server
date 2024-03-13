package com.example.exceptions

class ResourceNotFoundException(private val reason: String) : Throwable() {
    val body = InvalidInputResponse(reason)
}