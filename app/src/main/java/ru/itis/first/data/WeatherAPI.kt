package ru.itis.first.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "619e02b8ea1914192d66f5ebd34cfcd0",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru"
    ): retrofit2.Response<WeatherResponse>
}