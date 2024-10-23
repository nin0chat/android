package dev.nin0.chat.api.gateway.event

import dev.nin0.chat.api.model.Message
import dev.nin0.chat.api.model.User
import dev.nin0.chat.api.model.gateway.OpCode
import dev.nin0.chat.api.model.gateway.event.History
import dev.nin0.chat.api.model.gateway.event.Identify
import dev.nin0.chat.api.model.gateway.event.UserListSync
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonElement

interface Event

class EventDeserializationStrategy(
    private val opcode: OpCode
): DeserializationStrategy<Event> {

    override val descriptor: SerialDescriptor
        get() = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): Event {
        return when(opcode) {
            OpCode.Error -> ErrorEvent(decoder.decodeString())
            OpCode.SendOrReceiveMessage -> MessageEvent(decoder.decodeSerializableValue(Message.serializer()))
            OpCode.Identify -> IdentifyEvent(decoder.decodeSerializableValue(User.serializer()))
            OpCode.Heartbeat -> HeartbeatEvent
            OpCode.History -> MessageHistoryEvent(decoder.decodeSerializableValue(History.serializer()))
            OpCode.UserListSync -> UserListSyncEvent(decoder.decodeSerializableValue(UserListSync.serializer()))
            OpCode.Unknown -> throw IllegalStateException("Unknown event received")
        }
    }

}