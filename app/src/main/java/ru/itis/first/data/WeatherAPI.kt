package ru.itis.first.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = "f33d828605d977b5a6565ec54c32332",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru"
    ): retrofit2.Response<WeatherResponse>
}