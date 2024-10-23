package dev.nin0.chat.api.model.gateway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GatewayPayload<D>(
    @SerialName("op")  val opCode: OpCode,
    @SerialName("d")   val data: D?
)