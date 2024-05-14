package com.udyata.koin.stock

import com.udyata.koin.CommonRepository
import com.udyata.koin.RequestState
import com.udyata.koin.SessionManager
import com.udyata.koin.auth.GetTokenResponse
import com.udyata.koin.auth.OnTokenRequest
import com.udyata.koin.handleRequest


class AddStockUseCase(
    private val repository: CommonRepository
) {
    suspend fun invoke(onSaveItemStockRequest: OnSaveItemStockRequest): RequestState<GetSaveItemStockResponse> {
        return handleRequest(
            request = { repository.addStock(onSaveItemStockRequest) },
            transform = { it }
        )
    }
}
