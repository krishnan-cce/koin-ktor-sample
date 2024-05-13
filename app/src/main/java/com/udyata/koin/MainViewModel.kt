package com.udyata.koin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udyata.koin.UserDetails.UserDetailUseCase
import com.udyata.koin.UserDetails.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (
    private val locationUseCase: LocationUseCase,
    private val userDetailUseCase:UserDetailUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _locationUiState = MutableStateFlow<RequestState<LocationResponse>>(RequestState.Loading)
    val locationUiState: StateFlow<RequestState<LocationResponse>> = _locationUiState.asStateFlow()

    private val _userState = MutableStateFlow<RequestState<UserModel>>(RequestState.Loading)
    val userState: StateFlow<RequestState<UserModel>> = _userState.asStateFlow()

    init {
        Log.d("Session Logged In",sessionManager.isLogin.toString())
        getLocations()
        getUser()
    }

    private fun getUser() = viewModelScope.launch {
        _userState.value = RequestState.Loading
        _userState.value = userDetailUseCase.invoke()
    }


    private fun getLocations() = viewModelScope.launch {
        _locationUiState.value = RequestState.Loading
        _locationUiState.value = locationUseCase.invoke()
    }



}

