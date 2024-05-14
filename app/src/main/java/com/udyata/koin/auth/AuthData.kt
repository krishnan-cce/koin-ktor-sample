package com.udyata.koin.auth

import kotlinx.serialization.Serializable

@Serializable
data class GetTokenResponse(
    val accessToken: String?,
    val tokenType: String?,
    val userId:Int?
)
@Serializable
data class OnTokenRequest(
    val usernameOrEmail: String,
    val password: String
)
