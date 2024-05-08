package com.udyata.koin


interface CommonRepository {
    suspend fun getLocationMaster():NetworkResult<LocationMaster>
}

class CommonRepositoryImpl(
    private val requestHandler: RequestHandler
) : CommonRepository {

    override suspend fun getLocationMaster(): NetworkResult<LocationMaster>{
        return requestHandler.get(
            urlPathSegments = listOf("api", "location_master")
        )
    }
}

