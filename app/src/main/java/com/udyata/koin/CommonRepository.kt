package com.udyata.koin

import com.udyata.koin.UserDetails.UserApiModel


interface CommonRepository {
    suspend fun getLocationMaster():RequestState<LocationMaster>
    suspend fun getUserData():RequestState<UserApiModel>

}

class CommonRepositoryImpl(
    private val requestHandler: RequestHandler
) : CommonRepository {

    override suspend fun getLocationMaster(): RequestState<LocationMaster>{
        return try {
            requestHandler.get(
                urlPathSegments = listOf("api", "location_master")
            )
        } catch (e: Exception) {
            RequestState.Error("Unable to fetch location data. Please try again later.")
        }
    }

    override suspend fun getUserData(): RequestState<UserApiModel> {
        return try {
            requestHandler.get(
                urlPathSegments = listOf("api", "user"),
                queryParams = mapOf("userId" to 1)
            )
        } catch (e: Exception) {
            RequestState.Error("Unable to fetch user data. Please try again later.")
        }
    }

}

