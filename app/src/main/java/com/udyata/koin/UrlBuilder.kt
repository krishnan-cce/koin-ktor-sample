package com.udyata.koin

class UrlBuilder {

    fun buildLoginUrl():List<String> {
        return listOf("api","auth","login")
    }
    fun buildLocationMasterUrl(): List<String> {
        return listOf("api", "location_master")
    }

    fun buildUserDataUrl(): List<String> {
        return listOf("api", "user")
    }

    fun buildAddStockUrl():List<String> {
        return listOf("api","item_stock_master")

    }
}