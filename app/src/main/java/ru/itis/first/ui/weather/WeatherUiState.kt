package ru.itis.first.ui.weather

import ru.itis.first.data.model.WeatherResponse

sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
