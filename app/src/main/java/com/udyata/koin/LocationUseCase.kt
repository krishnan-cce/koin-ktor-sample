package com.udyata.koin

class LocationUseCase(
    private val repository: CommonRepository,
    private val mapper: LocationMapper
) {

    suspend fun invoke(): RequestState<LocationResponse> {
        return when (val result = repository.getLocationMaster()) {
            is RequestState.Error -> {
                RequestState.Error(result.message)
            }
            is RequestState.Success -> {
                val userModel = mapper.map(result.data)
                RequestState.Success(userModel)
            }
            else -> RequestState.Error("Unexpected state")
        }
    }

}

