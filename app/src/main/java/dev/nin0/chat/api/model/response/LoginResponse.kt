package dev.nin0.chat.api.model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: String,
    val token: String
)