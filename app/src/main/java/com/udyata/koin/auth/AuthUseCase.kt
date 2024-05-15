package com.udyata.koin.auth

import com.udyata.koin.CommonRepository
import com.udyata.koin.RequestState
import com.udyata.koin.SessionManager
import com.udyata.koin.UserDetails.UserModel
import com.udyata.koin.UserMapper
import com.udyata.koin.handleRequest
import com.udyata.koin.requestFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


//class AuthUseCase(
//    private val repository: CommonRepository,
//    private val sessionManager: SessionManager
//) {
//
//    suspend fun invoke(onRequest:OnTokenRequest): RequestState<GetTokenResponse> {
//        return when (val result = repository.onLogin(onRequest=onRequest)) {
//            is RequestState.Error -> {
//                RequestState.Error(result.message)
//            }
//            is RequestState.Success -> {
//                RequestState.Success(result.data)
//            }
//            else -> RequestState.Error("Unexpected state")
//        }
//    }
//}


class AuthUseCase(
    private val repository: CommonRepository
) {
    suspend fun invoke(onRequest: OnTokenRequest): RequestState<GetTokenResponse> {
        return handleRequest(
            request = { repository.onLogin(onRequest) },
            transform = { it }
        )
    }
}

