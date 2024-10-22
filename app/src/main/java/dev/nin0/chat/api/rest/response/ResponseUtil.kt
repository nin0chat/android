package dev.nin0.chat.api.rest.response

import dev.nin0.chat.api.model.ApiError

inline fun <D> HttpResponse<D>.ifSuccessful(block: (D) -> Unit) {
    if (this is HttpResponse.Success) block(data)
}

inline fun <D> HttpResponse<D>.ifError(block: (ApiError?, httpStatus: Int) -> Unit) {
    if (this is HttpResponse.Error) block(error, httpStatus)
}

inline fun <D> HttpResponse<D>.ifFailure(block: (Throwable?) -> Unit) {
    if (this is HttpResponse.Failure) block(throwable)
}

inline fun <D, T> HttpResponse<D>.transform(block: (D) -> T): HttpResponse<T> {
    return when (this) {
        is HttpResponse.Success -> HttpResponse.Success(block(data))
        is HttpResponse.Error -> HttpResponse.Error(error, httpStatus)
        is HttpResponse.Failure -> HttpResponse.Failure(throwable, body)
    }
}

/**
 * Unified method for acting on all [HttpResponse] states
 *
 * @param success Called when the request was successful
 * @param error Called when the server sent an error
 * @param failure Called when the client fails to process the response
 */
inline fun <D> HttpResponse<D>.fold(
    success: (D) -> Unit = {},
    error: (ApiError?, httpStatus: Int) -> Unit = { _, _ -> },
    failure: (Throwable?) -> Unit = {}
) {
    when (this) {
        is HttpResponse.Success -> success(data)
        is HttpResponse.Error -> error(this.error, httpStatus)
        is HttpResponse.Failure -> failure(throwable)
    }
}

fun <D> HttpResponse<D>.dataOrNull(): D?
    = if (this is HttpResponse.Success) data else null

/**
 * @throws IllegalStateException
 */
fun <D> HttpResponse<D>.dataOrThrow(): D
    = if (this is HttpResponse.Success) data else throw IllegalStateException("Response not successful")