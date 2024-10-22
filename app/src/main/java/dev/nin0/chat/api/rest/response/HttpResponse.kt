package dev.nin0.chat.api.rest.response

import dev.nin0.chat.api.model.ApiError

/**
 * Wrapper for safely handling http response data
 *
 * @param D The model to deserialize data into
 */
sealed interface HttpResponse<D> {

    /**
     * Represents a request that successfully returned data
     *
     * @param data The data returned from the server
     */
    data class Success<D>(val data: D): HttpResponse<D>

    /**
     * Represent a request that received an error response from the server
     *
     * @param error The error returned from Discord
     * @param httpStatus The http status message returned by the server
     */
    data class Error<D>(val error: ApiError?, val httpStatus: Int): HttpResponse<D>

    /**
     * Represents an error that occurred on the client while processing the response, usually due to an inaccurate model or lack of an internet connection
     *
     * @param throwable The error that was thrown during response processing
     * @param body The raw response body sent by the server
     */
    data class Failure<D>(val throwable: Throwable?, val body: String?): HttpResponse<D>

}