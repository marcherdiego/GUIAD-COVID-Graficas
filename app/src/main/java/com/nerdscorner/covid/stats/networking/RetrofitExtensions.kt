package com.nerdscorner.covid.stats.networking

import android.util.Log
import com.nerdscorner.covid.stats.exceptions.NetworkException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "NETWORK CALL"

private const val NULL_BODY_ERROR = "Body was null!"
private const val CACHE_RESPONSE_TYPE = "Cache"
private const val NETWORK_RESPONSE_TYPE = "Network"

fun <T> Call<T>.enqueueWithStatusCode(
    success: (T?, Int) -> Unit = { _: T?, _: Int -> },
    successCacheCheck: (Boolean, T?, Int) -> Unit = { _, _, _ -> },
    fail: (NetworkException) -> Unit = {}
): Call<T> {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                val body = response.body()
                val statusCode = response.code()
                success(body, statusCode)
                successCacheCheck(response.isFromCache(), body, statusCode)
                val responseOrigin = if (response.isFromCache()) {
                    CACHE_RESPONSE_TYPE
                } else {
                    NETWORK_RESPONSE_TYPE
                }
                Log.d(TAG, "${call.request().method}: ${call.request().url} -> Response from $responseOrigin")
            } else {
                fail(NetworkException(response = response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) {
                return
            }
            fail(NetworkException(message = t.message, throwable = t))
        }
    })
    return this
}

fun <T> Call<T>.enqueue(
    success: (T?) -> Unit = {},
    successCacheCheck: (Boolean, T?) -> Unit = { _, _ -> },
    fail: (NetworkException) -> Unit = {}
): Call<T> {
    enqueueWithStatusCode(
        success = { body, _ ->
            if (body != null) {
                success(body)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR))
            }
        },
        successCacheCheck = { fromCache, body, statusCode ->
            if (body != null) {
                successCacheCheck(fromCache, body)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR, statusCode = statusCode))
            }
        },
        fail = fail
    )
    return this
}

fun <T> Call<T>.enqueueResponseNotNull(
    success: (T) -> Unit = {},
    successCacheCheck: (Boolean, T) -> Unit = { _, _ -> },
    fail: (NetworkException) -> Unit = {}
): Call<T> {
    return enqueueWithStatusCode(
        success = { body, _ ->
            if (body != null) {
                success(body)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR))
            }
        },
        successCacheCheck = { fromCache, body, _ ->
            if (body != null) {
                successCacheCheck(fromCache, body)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR))
            }
        },
        fail = fail
    )
}

fun <T> Call<T>.enqueueResponseNotNullWithStatusCode(
    success: (T, Int) -> Unit = { _: T, _: Int -> },
    successCacheCheck: (Boolean, T, Int) -> Unit = { _, _, _ -> },
    fail: (NetworkException) -> Unit = {}
): Call<T> {
    return enqueueWithStatusCode(
        success = { body, statusCode ->
            if (body != null) {
                success(body, statusCode)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR))
            }
        },
        successCacheCheck = { fromCache, body, statusCode ->
            if (body != null) {
                successCacheCheck(fromCache, body, statusCode)
            } else {
                fail(NetworkException(message = NULL_BODY_ERROR, statusCode = statusCode))
            }
        },
        fail = fail
    )
}

fun <T> Call<T>.fireAndForget() {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {}
        override fun onResponse(call: Call<T>, response: Response<T>) {}
    })
}

fun Response<*>.isFromCache() = raw().networkResponse == null
