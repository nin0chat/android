package dev.nin0.chat.api.gateway

import android.util.Log
import dev.nin0.chat.api.model.gateway.GatewayPayload
import dev.nin0.chat.api.model.gateway.OpCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class GatewayLogger {

    val json = Json { prettyPrint = true }

    fun incomingPayload(payload: GatewayPayload<JsonElement>) {
        val msg = buildString {
            appendLine("<- ${payload.opCode.code} ${payload.opCode.name}")
            append(json.encodeToString(payload.data))
        }
        Log.v(TAG, msg)
    }

    inline fun <reified D> outgoingPayload(opCode: OpCode, data: D) {
        val msg = buildString {
            appendLine("-> ${opCode.code} ${opCode.name}")
            append(json.encodeToString(data))
        }
        Log.v(TAG, msg)
    }

    fun error(message: String, throwable: Throwable) {
        Log.e(TAG, message, throwable)
    }

    companion object {
        const val TAG = "Gateway"
    }

}