package dev.nin0.chat.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("userInfo") val author: User,
    val timestamp: UnixInstant,
    val content: String,
    val id: String,
    val device: Device = Device.Unknown,
    val type: MessageType = MessageType.Unknown
)