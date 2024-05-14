package com.udyata.koin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udyata.koin.UserDetails.UserDetailUseCase
import com.udyata.koin.UserDetails.UserModel
import com.udyata.koin.auth.AuthUseCase
import com.udyata.koin.auth.GetTokenResponse
import com.udyata.koin.auth.OnTokenRequest
import com.udyata.koin.stock.AddStockUseCase
import com.udyata.koin.stock.GetSaveItemStockResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (
    private val authUseCase: AuthUseCase,
    private val locationUseCase: LocationUseCase,
    private val userDetailUseCase:UserDetailUseCase,
    private val sessionManager: SessionManager,
    private val addStockUseCase: AddStockUseCase
) : ViewModel() {

    private val _authUiState = MutableStateFlow<RequestState<GetTokenResponse>>(RequestState.Idle)
    val authUiState: StateFlow<RequestState<GetTokenResponse>> = _authUiState.asStateFlow()

    private val _addStockUiState = MutableStateFlow<RequestState<GetSaveItemStockResponse>>(RequestState.Idle)
    val addStockUiState: StateFlow<RequestState<GetSaveItemStockResponse>> = _addStockUiState.asStateFlow()

    private val _locationUiState = MutableStateFlow<RequestState<LocationResponse>>(RequestState.Idle)
    val locationUiState: StateFlow<RequestState<LocationResponse>> = _locationUiState.asStateFlow()

    private val _userState = MutableStateFlow<RequestState<UserModel>>(RequestState.Idle)
    val userState: StateFlow<RequestState<UserModel>> = _userState.asStateFlow()

    init {
        Log.d("Session Logged In",sessionManager.isLogin.toString())
        getLocations()
        getUser()
    }
    fun onAddStock() = viewModelScope.launch{
        _addStockUiState.value = RequestState.Loading
        _addStockUiState.value = addStockUseCase.invoke()
    }
    fun onLogin(onRequest:OnTokenRequest) = viewModelScope.launch{
        _authUiState.value = RequestState.Loading
        _authUiState.value = authUseCase.invoke(onRequest=onRequest)
    }

     fun getUser() = viewModelScope.launch {
        _userState.value = RequestState.Loading
        _userState.value = userDetailUseCase.invoke()
    }


    private fun getLocations() = viewModelScope.launch {
        _locationUiState.value = RequestState.Loading
        _locationUiState.value = locationUseCase.invoke()
    }



}

