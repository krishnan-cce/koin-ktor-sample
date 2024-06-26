package com.udyata.koin.UserDetails

import com.udyata.koin.CommonRepository
import com.udyata.koin.LocationMapper
import com.udyata.koin.LocationResponse
import com.udyata.koin.RequestState
import com.udyata.koin.UserMapper
import com.udyata.koin.handleRequest
import com.udyata.koin.requestFlow
import kotlinx.coroutines.flow.Flow


//class UserDetailUseCase(
//    private val repository: CommonRepository,
//    private val mapper: UserMapper
//) {
//
//    suspend fun invoke(): RequestState<UserModel> {
//        return when (val result = repository.getUserData()) {
//            is RequestState.Error -> {
//                RequestState.Error(result.message)
//            }
//            is RequestState.Success -> {
//                val userModel = mapper.map(result.data)
//                RequestState.Success(userModel)
//            }
//            else -> RequestState.Error("Unexpected state")
//        }
//    }
//}

class UserDetailUseCase(
    private val repository: CommonRepository,
    private val mapper: UserMapper
) {
    fun invoke(): Flow<RequestState<UserModel>> = requestFlow(
        request = { repository.getUserData() },
        transform = { mapper.map(it) }
    )
}
