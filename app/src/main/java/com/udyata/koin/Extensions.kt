package com.udyata.koin

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T> executeWithStateFlow(
    stateFlow: MutableStateFlow<RequestState<T>>,
    action: suspend () -> RequestState<T>
) {
    stateFlow.value = RequestState.Loading
    stateFlow.value = action()
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
