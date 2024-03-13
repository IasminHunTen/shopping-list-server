package com.example.exceptions

class InvalidInputException(private val reasons: String, private val fieldName: String? = null) : Throwable() {
    val body = InvalidInputResponse(reasons, fieldName)
}