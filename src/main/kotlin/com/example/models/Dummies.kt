package com.example.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface Dummies {
    @Serializable
    data class Good(val response: Int)
    @Serializable
    data class Bad(val message: String)
}