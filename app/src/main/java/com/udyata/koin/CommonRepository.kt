package com.udyata.koin

import android.util.Log
import com.udyata.koin.UserDetails.UserApiModel
import com.udyata.koin.auth.GetTokenResponse
import com.udyata.koin.auth.OnTokenRequest
import com.udyata.koin.stock.GetSaveItemStockResponse
import com.udyata.koin.stock.OnSaveItemStockRequest


interface CommonRepository {

    suspend fun onLogin(onRequest:OnTokenRequest):RequestState<GetTokenResponse>
    suspend fun getLocationMaster():RequestState<LocationMaster>
    suspend fun getUserData():RequestState<UserApiModel>
    suspend fun addStock(onSaveItemStockRequest: OnSaveItemStockRequest):RequestState<GetSaveItemStockResponse>

}

class CommonRepositoryImpl(
    private val requestHandler: RequestHandler
) : CommonRepository {
    override suspend fun onLogin(onRequest: OnTokenRequest): RequestState<GetTokenResponse> {
        return try {
            requestHandler.post(
                urlPathSegments = UrlBuilder().buildLoginUrl(),
                body = mapOf("usernameOrEmail" to onRequest.usernameOrEmail, "password" to onRequest.password)
            )
        }catch (e: Exception){
            RequestState.Error("Unable to fetch token. Please try again later.")
        }
    }

    override suspend fun getLocationMaster(): RequestState<LocationMaster>{
        return try {
            requestHandler.get(
                urlPathSegments = UrlBuilder().buildLocationMasterUrl()
            )
        } catch (e: Exception) {
            RequestState.Error("Unable to fetch location data. Please try again later.")
        }
    }

    override suspend fun getUserData(): RequestState<UserApiModel> {
        return try {
            requestHandler.get(
                urlPathSegments = UrlBuilder().buildUserDataUrl(),
                queryParams = mapOf("userId" to 1)
            )
        } catch (e: Exception) {
            RequestState.Error("Unable to fetch user data. Please try again later.")
        }
    }

    override suspend fun addStock(onSaveItemStockRequest: OnSaveItemStockRequest): RequestState<GetSaveItemStockResponse> {
        return try {
            Log.d("CommonRepositoryImpl", "addStock: $onSaveItemStockRequest")
            requestHandler.post(
                urlPathSegments = UrlBuilder().buildAddStockUrl(),
                body = onSaveItemStockRequest
            )
        }catch (e: Exception){
            RequestState.Error("Unable to add stock. Please try again later.")
        }
    }

}

