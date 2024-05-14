package com.udyata.koin.stock

import com.udyata.koin.CommonRepository
import com.udyata.koin.RequestState
import com.udyata.koin.SessionManager
import com.udyata.koin.auth.GetTokenResponse
import com.udyata.koin.auth.OnTokenRequest


class AddStockUseCase(
    private val repository: CommonRepository
) {

    suspend fun invoke(): RequestState<GetSaveItemStockResponse> {
        val onSaveItemStockRequest =  OnSaveItemStockRequest(
            addedBy = 1,
            physicalQuantity = 1.0,
            itemId = 30,
            locationId = 10
        )
        return when (val result = repository.addStock(onSaveItemStockRequest =onSaveItemStockRequest)) {
            is RequestState.Error -> {
                RequestState.Error(result.message)
            }
            is RequestState.Success -> {
                RequestState.Success(result.data)
            }
            else -> RequestState.Error("Unexpected state")
        }
    }
}
