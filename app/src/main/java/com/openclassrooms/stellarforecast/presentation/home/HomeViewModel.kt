package com.openclassrooms.stellarforecast.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.stellarforecast.data.repository.WeatherRepository
import com.openclassrooms.stellarforecast.domain.model.WeatherReportModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val dataRepository: WeatherRepository) :
    ViewModel() {

    // Expose screen UI state
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getForecastData()
    }

    private fun getForecastData() {
        val latitude = 48.844304
        val longitude = 2.374377
        dataRepository.fetchForecastData(latitude, longitude).onEach { forecastUpdate ->
            _uiState.update { currentState ->
                currentState.copy(
                    forecast = forecastUpdate,
                )
            }
        }.launchIn(viewModelScope)
    }
}

data class HomeUiState(
    val forecast: List<WeatherReportModel> = emptyList(),
)
