package ru.itis.first.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.itis.first.data.RetrofitClient

class WeatherViewModel : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading
            try {
                val response = RetrofitClient.instance.getWeather(city)
                if (response.isSuccessful && response.body() != null) {
                    _state.value = WeatherUiState.Success(response.body()!!)
                } else {
                    _state.value = WeatherUiState.Error("Ошибка сервера: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = WeatherUiState.Error("Ошибка сети: ${e.message}")
            }
        }
    }
}
