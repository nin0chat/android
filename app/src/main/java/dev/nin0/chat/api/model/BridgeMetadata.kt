package dev.nin0.chat.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BridgeMetadata(
    @SerialName("from") val source: String? = null,
    val color: String? = null
)