package ru.itis.first.data.model

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)