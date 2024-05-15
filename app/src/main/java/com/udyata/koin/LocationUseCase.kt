package com.udyata.koin

import com.udyata.koin.auth.GetTokenResponse
import com.udyata.koin.auth.OnTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationUseCase(
    private val repository: CommonRepository,
    private val mapper: LocationMapper
) {
    fun invoke(): Flow<RequestState<LocationResponse>> = requestFlow(
        request = { repository.getLocationMaster() },
        transform = { mapper.map(it) }
    )
}
