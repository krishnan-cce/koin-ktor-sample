package com.udyata.koin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _locationData = MutableStateFlow<Resource<LocationResponse>?>(null)
    val locationData: StateFlow<Resource<LocationResponse>?> = _locationData.asStateFlow()

    private val _uiState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val uiState: StateFlow<LocationUiState> = _uiState


    init {
        getLocations()
    }

    private fun fetchLocationData() {
        viewModelScope.launch {
            _locationData.value = locationUseCase.invoke()
        }
    }

    private fun getLocations() = viewModelScope.launch {
        when (val data = locationUseCase.invoke()) {
            is Resource.Error -> _uiState.value = LocationUiState.Error
            is Resource.Success -> _uiState.value = LocationUiState.Location(data.result)
        }
    }


}


sealed class LocationUiState {
    data object Loading : LocationUiState()
    data object Error : LocationUiState()
    data class Location(val data: LocationResponse) : LocationUiState()
}
