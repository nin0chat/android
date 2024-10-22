package dev.nin0.chat.api.rest

import dev.nin0.chat.api.model.request.LoginBody
import dev.nin0.chat.api.model.response.LoginResponse
import dev.nin0.chat.api.rest.request.RequestHandler
import dev.nin0.chat.api.rest.request.Routes
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NinoChatService(
    private val requestHandler: RequestHandler
) {

    suspend fun login(
        email: String,
        password: String
    ) = requestHandler.post<LoginResponse>(Routes.Auth.Login) {
        contentType(ContentType.Application.Json)

        setBody(LoginBody(email, password))
    }

}