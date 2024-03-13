package com.example.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class InvalidInputResponse(
    val reason: String,
    val fieldName: String? = null
)
