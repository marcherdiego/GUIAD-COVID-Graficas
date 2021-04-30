package com.nerdscorner.covid.stats.exceptions

import retrofit2.Response

class NetworkException(
    message: String? = null,
    var response: Response<*>? = null,
    var throwable: Throwable? = null,
    var statusCode: Int? = null
) : Exception(message ?: response?.message()) {
    var errorCode: String? = null
    var errorMessage: String? = null
    var errorBody: String? = null

    init {
        response?.let {
            statusCode = it.code()
            errorCode = it.headers()[X_ERROR_CODE]
            errorBody = it.errorBody()?.string()
            errorMessage = it.headers()[X_ERROR_MESSAGE]
        }
    }

    companion object {
        private const val X_ERROR_CODE = "X-ERROR-CODE"
        private const val X_ERROR_MESSAGE = "X-ERROR-MESSAGE"
    }
}
