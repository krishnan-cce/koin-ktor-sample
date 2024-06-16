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
) {
    fun filter(filter: Filter): List<LocationResponseData>? {
        return when (filter) {
            is Filter.ByQuery -> data?.filter { it.locationName?.contains(filter.query, ignoreCase = true) == true }
            Filter.None -> data
        }
    }
    private fun sortByName(data: List<LocationResponseData>?, sortOrder: SortOrder): List<LocationResponseData>? {
        return when (sortOrder) {
            SortOrder.ASCENDING -> data?.sortedBy { it.locationName }
            SortOrder.DESCENDING -> data?.sortedByDescending { it.locationName }
        }
    }

    fun sort(data: List<LocationResponseData>?, sort: Sort, sortOrder: SortOrder): List<LocationResponseData>? {
        return when (sort) {
            is Sort.ByName -> sortByName(data, sortOrder)
            Sort.None -> data
        }
    }

    fun group(data: List<LocationResponseData>?, group: Group): Map<Char, List<LocationResponseData>>? {
        return when (group) {
            is Group.ByFirstLetter -> data?.groupBy { it.locationName?.firstOrNull() ?: '#' }
            Group.None -> null
        }
    }

}

data class LocationResponseData(
    val addDate: String?,
    val addedBy: Int?,
    val address: String?,
    val id: Int?,
    val locationCode: String?,
    val locationName: String?,
    val mobile: String?
)


enum class SortOrder {
    ASCENDING,
    DESCENDING
}

sealed class Filter {
    data class ByQuery(val query: String) : Filter()
    data object None : Filter()
}

sealed class Sort {
    data object ByName : Sort()
    data object None : Sort()
}

sealed class Group {
    data object ByFirstLetter : Group()
    data object None : Group()
}
