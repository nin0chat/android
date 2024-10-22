package dev.nin0.chat.di

import android.util.Log
import dev.nin0.chat.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.WebSocketDeflateExtension
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val KtorModule = module {

    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(CIO) {
            // Websocket
            install(WebSockets) {
                maxFrameSize = Long.MAX_VALUE

                extensions {
                    install(WebSocketDeflateExtension) {
                        compressIf { false }
                        clientNoContextTakeOver = false
                    }
                }
            }

            // Request retrying
            install(HttpRequestRetry) {
                maxRetries = 5

                retryIf { _, res ->
                    res.status.value in 500..< 600
                }

                retryOnExceptionIf { _, error ->
                    error is HttpRequestTimeoutException
                }

                delayMillis { retryCount ->
                    retryCount * 2000L
                }
            }

            defaultRequest {
                header(HttpHeaders.UserAgent, "nin0chat Android v1.0.0") // TODO: Dynamically generate
            }

            install(ContentNegotiation) {
                json(json)
            }

            if (BuildConfig.DEBUG) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d("HTTP", message)
                        }
                    }
                }
            }
        }
    }

    singleOf(::provideJson)
    singleOf(::provideHttpClient)

}