package com.udyata.koin

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
