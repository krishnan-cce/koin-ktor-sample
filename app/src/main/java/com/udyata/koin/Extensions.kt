package com.udyata.koin

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

// helper fun for stateFlow
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
suspend fun <T> executeWithStateFlow(
    stateFlow: MutableStateFlow<RequestState<T>>,
    action: suspend () -> RequestState<T>
): RequestState<T> {
    stateFlow.value = RequestState.Loading
    val result = action()
    stateFlow.value = result
    return result
}



// Helper fun for flow
fun <T, R> requestFlow(
    request: suspend () -> RequestState<T>,
    transform: (T) -> R
): Flow<RequestState<R>> = flow {
    emit(RequestState.Loading)
    when (val result = request()) {
        is RequestState.Error -> emit(RequestState.Error(result.message))
        is RequestState.Success -> emit(RequestState.Success(transform(result.data)))
        else -> emit(RequestState.Error("Unexpected state"))
    }
}.flowOn(Dispatchers.IO)

fun <T> collectAndSetState(
    stateFlow: MutableStateFlow<RequestState<T>>,
    flow: Flow<RequestState<T>>,
    scope: CoroutineScope
) {
    scope.launch {
        flow.collectLatest { response ->
            stateFlow.value = response
        }
    }
}

// Logger For Network Calls
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
