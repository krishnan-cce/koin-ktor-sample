package com.udyata.koin

import android.nfc.Tag
import android.util.Log

class LocationUseCase(
    private val repository: CommonRepository,
    private val mapper: LocationMapper
) {

    suspend fun invoke(): Resource<LocationResponse> {
        return when (val result = repository.getLocationMaster()) {
            is NetworkResult.Error -> {
                result.toResourceError()
            }

            is NetworkResult.Success -> {
                Resource.Success(mapper.map(result.result))
            }
        }
    }
}


