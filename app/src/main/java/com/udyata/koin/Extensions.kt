package com.udyata.koin

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T> executeWithStateFlow(
    stateFlow: MutableStateFlow<RequestState<T>>,
    action: suspend () -> RequestState<T>
): RequestState<T> {
    stateFlow.value = RequestState.Loading
    val result = action()
    stateFlow.value = result
    return result
}

suspend fun <T, R> handleRequest(
    request: suspend () -> RequestState<T>,
    transform: (T) -> R
): RequestState<R> {
    return when (val result = request()) {
        is RequestState.Error -> RequestState.Error(result.message)
        is RequestState.Success -> RequestState.Success(transform(result.data))
        else -> RequestState.Error("Unexpected state")
    }
}


fun <R> logRequestState(
    result: RequestState<R>,
    requestType: String,
    urlPathSegments: List<Any>
) {
    val url = urlPathSegments.joinToString("/")
    when (result) {
        is RequestState.Success -> Log.d("RequestHandler", "$requestType request to $url successful with response: ${result.data}")
        is RequestState.Error -> Log.d("RequestHandler", "$requestType request to $url failed with error: ${result.message}")
        RequestState.Idle -> Log.d("RequestHandler", "$requestType request to $url is idle")
        RequestState.Loading -> Log.d("RequestHandler", "$requestType request to $url is loading")
    }
}
