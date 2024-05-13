package com.udyata.koin

class UrlBuilder {
    fun buildLocationMasterUrl(): List<String> {
        return listOf("api", "location_master")
    }

    fun buildUserDataUrl(): List<String> {
        return listOf("api", "user")
    }
}