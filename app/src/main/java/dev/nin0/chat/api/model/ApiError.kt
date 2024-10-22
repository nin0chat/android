package dev.nin0.chat.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val error: String
)
