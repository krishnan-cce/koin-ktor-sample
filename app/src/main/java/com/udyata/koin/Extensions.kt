package com.udyata.koin

import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T> executeWithStateFlow(
    stateFlow: MutableStateFlow<RequestState<T>>,
    action: suspend () -> RequestState<T>
) {
    stateFlow.value = RequestState.Loading
    stateFlow.value = action()
}