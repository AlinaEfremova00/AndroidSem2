package ru.itis.first.data.model

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int?,
    val sunset: Int?,
    val type: Int?
)