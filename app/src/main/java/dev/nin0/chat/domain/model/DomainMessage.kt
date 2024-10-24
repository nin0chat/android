package dev.nin0.chat.domain.model

import dev.nin0.chat.api.model.Device
import dev.nin0.chat.api.model.Message
import dev.nin0.chat.api.model.MessageType
import kotlinx.datetime.Instant

class DomainMessage(
    val author: DomainUser,
    val timestamp: Instant,
    val content: String,
    val id: String,
    val device: Device? = Device.Unknown,
    val type: MessageType = MessageType.Unknown,
    val bridgeMetadata: DomainBridgeMetadata?
) {

    companion object {

        fun fromApi(apiMessage: Message, bridgeClientName: String = "") = with(apiMessage) {
            DomainMessage(
                author = DomainUser.fromApi(apiMessage.author),
                timestamp = timestamp,
                content = content,
                id = id,
                device = device,
                type = type,
                bridgeMetadata = apiMessage.author.bridgeMetadata?.let { DomainBridgeMetadata.fromApi(it, bridgeClientName) }
            )
        }

    }

}