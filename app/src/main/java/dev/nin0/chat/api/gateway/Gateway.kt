package dev.nin0.chat.api.gateway

import dev.nin0.chat.BuildConfig
import dev.nin0.chat.api.model.Device
import dev.nin0.chat.api.gateway.event.Event
import dev.nin0.chat.api.model.gateway.GatewayPayload
import dev.nin0.chat.api.model.gateway.OpCode
import dev.nin0.chat.api.gateway.event.EventDeserializationStrategy
import dev.nin0.chat.api.gateway.event.HeartbeatEvent
import dev.nin0.chat.api.gateway.event.IdentifyEvent
import dev.nin0.chat.api.model.gateway.payload.Identify
import dev.nin0.chat.api.model.gateway.payload.SendMessage
import dev.nin0.chat.domain.manager.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.coroutines.CoroutineContext

/**
 * An implementation for the nin0chat Gateway
 *
 * Handles authentication, parsing, and connection maintenance
 */
class Gateway(
    private val httpClient: HttpClient,
    private val authManager: AuthManager,
    private val json: Json
): CoroutineScope {

    // ===== Connecting =====

    private lateinit var wsSession: DefaultWebSocketSession

    // ======================


    // ===== Internal Gateway State =====

    private var startTime: Long? = null
    private val logger: GatewayLogger = GatewayLogger()

    // ==================================


    // ===== Backing Properties =====

    private val _events = MutableSharedFlow<Event>()
    private val _state = MutableStateFlow<GatewayState>(GatewayState.Uninitialized)

    // ==============================


    // ===== Public Properties =====

    val events: SharedFlow<Event> = _events.asSharedFlow()
    val state: SharedFlow<GatewayState> = _state.asSharedFlow()

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Default

    // =============================



    /**
     * Connects to the Gateway api
     */
    suspend fun connect() {
        if (_state.replayCache.lastOrNull()?.isActive == true) return

        startTime = System.currentTimeMillis()
        _state.emit(GatewayState.Started)

        try {
            wsSession = httpClient.webSocketSession(BuildConfig.SOCKET_URL)

            _state.emit(GatewayState.Connected)
            identify()
            listen()

            val reason = withTimeoutOrNull(2000L) {
                wsSession.closeReason.await()
            }

            _state.emit(GatewayState.Stopped)
        } catch (e: Throwable) {
            logger.error("Failed to connect to gateway", e)
        }
    }

    /**
     * Listens to incoming events and handles parsing
     * and emitting for easy consumption elsewhere
     */
    private suspend fun listen() {
        wsSession.incoming
            .receiveAsFlow()
            .buffer(Channel.UNLIMITED)
            .mapNotNull { frame ->
                try {
                    val text = when (frame) {
                        is Frame.Text -> frame.readText()
                        else -> throw IllegalStateException("Unsupported frame type received")
                    }

                    json.decodeFromString<GatewayPayload<JsonElement>>(text)
                } catch (e: Throwable) {
                    logger.error("Couldn't decode payload", e)
                    null
                }
            }
            .collect { payload ->
                logger.incomingPayload(payload)

                val parsedPayload = try {
                    json
                        .decodeFromJsonElement(
                            EventDeserializationStrategy(payload.opCode),
                            payload.data!!
                        )
                } catch (e: Throwable) {
                    logger.error("Failed to decode payload", e)
                    null
                }

                parsedPayload?.let { event ->
                    when (event) {
                        is HeartbeatEvent -> heartbeat()
                        is IdentifyEvent -> _state.emit(GatewayState.Authenticated)
                    }

                    _events.emit(event)
                }
            }
    }

    /**
     * Sends a payload to the server
     */
    private suspend inline fun <reified D> sendPayload(opCode: OpCode, data: D) {
        if (!opCode.canSend) throw IllegalArgumentException("Payload with opcode ${opCode.code} (${opCode.name}) cannot be sent by the client")
        logger.outgoingPayload(opCode, data)

        wsSession.send(json.encodeToString(GatewayPayload(opCode, data)))
    }

    /**
     * Authenticates the session either as a guest or as the logged
     * in user
     */
    private suspend fun identify() {
        sendPayload(
            opCode = OpCode.Identify,
            data = Identify(
                anon = !authManager.isLoggedIn,
                token = authManager.token,
                device = Device.Mobile
            )
        )
    }

    /**
     * Sends a heartbeat back to the server
     */
    private suspend fun heartbeat() {
        sendPayload<JsonElement?>(
            opCode = OpCode.Heartbeat,
            data = null
        )
    }

    /**
     * Sends a message as the identified user
     *
     * @param content The content of the message
     */
    suspend fun sendMessage(content: String) {
        sendPayload(
            opCode = OpCode.SendOrReceiveMessage,
            data = SendMessage(content)
        )
    }

}