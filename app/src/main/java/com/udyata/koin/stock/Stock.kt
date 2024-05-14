package com.udyata.koin.stock

import kotlinx.serialization.Serializable
@Serializable
data class OnSaveItemStockRequest(
    val addedBy: Int,
    val itemId: Int,
    val locationId: Int,
    val physicalQuantity: Double
)
@Serializable
data class GetSaveItemStockResponse(
    val addDate: String?,
    val addedBy: Int?,
    val id: Int?,
    val itemCode: String?,
    val itemId: Int?,
    val itemName: String?,
    val lastUpdated: String?,
    val locationId: Int?,
    val physicalQuantity: Double?,
    val unit: String?,
    val unitId: Int?
)