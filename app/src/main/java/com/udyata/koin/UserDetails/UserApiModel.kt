package com.udyata.koin.UserDetails

import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val data: UserApiModelData?,
    val message: String?,
    val status: Boolean?,
    val statusCode: Int?
)
@Serializable
data class UserApiModelData(
    val addDate: String?,
    val email: String?,
    val name: String?,
    val password: String?,
    val userId: Int?,
    val username: String?
)


data class UserModel(
    val data: UserModelData?,
    val message: String?,
    val status: Boolean?,
    val statusCode: Int?
)

data class UserModelData(
    val addDate: String?,
    val email: String?,
    val name: String?,
    val password: String?,
    val userId: Int?,
    val username: String?
)