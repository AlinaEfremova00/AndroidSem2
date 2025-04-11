package ru.itis.first.data

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)