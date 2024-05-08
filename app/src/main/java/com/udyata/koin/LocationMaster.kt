package com.udyata.koin

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class LocationMaster(
    val data: List<AllLocationMasterResponseData>?,
    val message: String?,
    val status: Boolean?,
    val statusCode: Int?
)

@Serializable
data class AllLocationMasterResponseData(
    val addDate: String?,
    val addedBy: Int?,
    val address: String?,
    val id: Int?,
    val locationCode: String?,
    val locationName: String?,
    val mobile: String?
)


data class LocationResponse(
    val data: List<LocationResponseData>?,
    val message: String?,
    val status: Boolean?,
    val statusCode: Int?
)

data class LocationResponseData(
    val addDate: String?,
    val addedBy: Int?,
    val address: String?,
    val id: Int?,
    val locationCode: String?,
    val locationName: String?,
    val mobile: String?
)