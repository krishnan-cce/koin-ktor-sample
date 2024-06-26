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
import com.udyata.koin.stock.OnSaveItemStockRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel (
    private val authUseCase: AuthUseCase,
    private val locationUseCase: LocationUseCase,
    private val userDetailUseCase:UserDetailUseCase,
    private val sessionManager: SessionManager,
    private val addStockUseCase: AddStockUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = _state.asStateFlow()


    private val _authUiState = MutableStateFlow<RequestState<GetTokenResponse>>(RequestState.Idle)
    val authUiState: StateFlow<RequestState<GetTokenResponse>> = _authUiState.asStateFlow()

    private val _addStockUiState = MutableStateFlow<RequestState<GetSaveItemStockResponse>>(RequestState.Idle)
    val addStockUiState: StateFlow<RequestState<GetSaveItemStockResponse>> = _addStockUiState.asStateFlow()

    private val _userState = MutableStateFlow<RequestState<UserModel>>(RequestState.Idle)
    val userState: StateFlow<RequestState<UserModel>> = _userState.asStateFlow()

    init {
        Log.d("Session Logged In",sessionManager.isLogin.toString())
        getLocations()
        getUser()
    }
    fun onAddStock() = viewModelScope.launch {
        val onSaveItemStockRequest =  OnSaveItemStockRequest(
            addedBy = 1,
            physicalQuantity = 1.0,
            itemId = 30,
            locationId = 10
        )
        executeWithStateFlow(_addStockUiState) { addStockUseCase.invoke(onSaveItemStockRequest) }
    }
    fun onLogin(onRequest: OnTokenRequest) = viewModelScope.launch {
        val result = executeWithStateFlow(_authUiState) { authUseCase.invoke(onRequest) }
        if (result is RequestState.Success) {
            sessionManager.jwtToken = result.data.accessToken.toString()
        }
    }
    fun getUser() = collectAndSetState(_userState, userDetailUseCase.invoke(), viewModelScope)
    private fun getLocations() = viewModelScope.launch {
        locationUseCase.invoke().collect { result ->
            _state.update { it.copy(locationUiState = result) }
        }
    }
    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.UpdateSearchQuery -> { _state.update { it.copy(searchQuery = event.query) } }
            LocationEvent.ToggleSortType -> {
                _state.update { state ->
                    val newSortType = if (state.isSortingEnabled) Sort.None else Sort.ByName
                    state.copy(
                        sortType = newSortType,
                        isSortingEnabled = !state.isSortingEnabled
                    )
                }
            }
            LocationEvent.ToggleSortOrder -> {
                _state.update { state ->
                    val newSortOrder = if (state.sortOrder == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
                    state.copy(sortOrder = newSortOrder)
                }
            }
            LocationEvent.ToggleGroupType -> {
                _state.update { state ->
                    val newGroupType = if (state.isGroupingEnabled) Group.None else Group.ByFirstLetter
                    state.copy(
                        groupType = newGroupType,
                        isGroupingEnabled = !state.isGroupingEnabled
                    )
                }
            }
        }
    }


}


sealed interface LocationEvent {
    data class UpdateSearchQuery(val query: String) : LocationEvent
    data object ToggleSortType : LocationEvent
    data object ToggleSortOrder : LocationEvent
    data object ToggleGroupType : LocationEvent
}

data class LocationState(
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val sortType: Sort = Sort.None,
    val groupType: Group = Group.None,
    val isSortingEnabled: Boolean = false,
    val isGroupingEnabled: Boolean = false,
    val locationUiState: RequestState<LocationResponse> = RequestState.Idle
)
