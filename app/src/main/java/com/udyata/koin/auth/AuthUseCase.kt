package com.udyata.koin.auth

import com.udyata.koin.CommonRepository
import com.udyata.koin.RequestState
import com.udyata.koin.SessionManager
import com.udyata.koin.UserDetails.UserModel
import com.udyata.koin.UserMapper


class AuthUseCase(
    private val repository: CommonRepository,
    private val sessionManager: SessionManager
) {

    suspend fun invoke(onRequest:OnTokenRequest): RequestState<GetTokenResponse> {
        return when (val result = repository.onLogin(onRequest=onRequest)) {
            is RequestState.Error -> {
                RequestState.Error(result.message)
            }
            is RequestState.Success -> {
                RequestState.Success(result.data)
                    .also {  sessionManager.jwtToken = result.data.accessToken.toString() }
            }
            else -> RequestState.Error("Unexpected state")
        }
    }
}
