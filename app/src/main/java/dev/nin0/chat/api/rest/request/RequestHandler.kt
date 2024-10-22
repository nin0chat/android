package dev.nin0.chat.api.rest.request

import dev.nin0.chat.api.model.ApiError
import dev.nin0.chat.api.rest.response.HttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class RequestHandler(
    val httpClient: HttpClient,
    val json: Json
) {

    /**
     * Ktor wrapper for safer handling of requests and responses
     *
     * @param route The route to make the request to
     * @param method The HTTP method to use for this request
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> request(
        route: Route,
        method: HttpMethod = HttpMethod.Get,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = withContext(Dispatchers.IO) {
        var body: String? = null

        val response = try {
            val response = httpClient.request {
                url(route.url)
                this.method = method

                builder?.invoke(this)
            }

            if (response.status.isSuccess()) {
                body = response.bodyAsText()

                if (D::class == String::class) {
                    HttpResponse.Success(body as D)
                } else {
                    HttpResponse.Success(json.decodeFromString<D>(body))
                }
            } else {
                body = response.bodyAsText()

                HttpResponse.Error(json.decodeFromString<ApiError>(body), response.status.value)
            }
        } catch (e: Throwable) {
            HttpResponse.Failure(e, body)
        }

        response
    }

    /**
     * Makes a GET request to the specified [route]
     *
     * @param route The route to make the request to
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> get(
        route: Route,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = request(route, HttpMethod.Get, builder = builder)

    /**
     * Makes a POST request to the specified [route]
     *
     * @param route The route to make the request to
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> post(
        route: Route,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = request(route, HttpMethod.Post, builder = builder)

    /**
     * Makes a PUT request to the specified [route]
     *
     * @param route The route to make the request to
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> put(
        route: Route,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = request(route, HttpMethod.Put, builder = builder)

    /**
     * Makes a PATCH request to the specified [route]
     *
     * @param route The route to make the request to
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> patch(
        route: Route,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = request(route, HttpMethod.Patch, builder = builder)

    /**
     * Makes a DELETE request to the specified [route]
     *
     * @param route The route to make the request to
     * @param builder Used to make any necessary alterations to the request, such as adding a body
     */
    suspend inline fun <reified D> delete(
        route: Route,
        noinline builder: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse<D> = request(route, HttpMethod.Delete, builder = builder)

}