package com.udyata.koin

import com.udyata.koin.UserDetails.UserApiModel
import com.udyata.koin.UserDetails.UserModel
import com.udyata.koin.UserDetails.UserModelData

interface Mapper<F, T> {
    fun map(from: F): T
}


class LocationMapper : Mapper<LocationMaster, LocationResponse> {
    override fun map(from: LocationMaster): LocationResponse {
        val responseData = from.data?.map { dataItem ->
            LocationResponseData(
                addDate = dataItem.addDate,
                addedBy = dataItem.addedBy,
                address = dataItem.address,
                id = dataItem.id,
                locationCode = dataItem.locationCode,
                locationName = dataItem.locationName,
                mobile = dataItem.mobile
            )
        }
        return LocationResponse(
            data = responseData,
            message = from.message,
            status = from.status,
            statusCode = from.statusCode
        )
    }
}


class UserMapper : Mapper<UserApiModel, UserModel> {
    override fun map(from: UserApiModel): UserModel {

        val data = UserModelData(
            addDate = from.data?.addDate,
            email = from.data?.email,
            name =  from.data?.name,
            password = from.data?.password,
            userId = from.data?.userId,
            username = from.data?.username,
        )

        return UserModel(
            data = data,
            message = from.message,
            status = from.status,
            statusCode = from.statusCode
        )
    }
}
