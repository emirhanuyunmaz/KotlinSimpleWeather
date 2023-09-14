package com.emirhanuyunmaz.kotlinsimpleweather.weather_model

import androidx.room.Entity


data class Main(
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)