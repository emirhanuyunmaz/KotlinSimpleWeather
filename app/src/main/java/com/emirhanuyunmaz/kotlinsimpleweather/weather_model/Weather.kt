package com.emirhanuyunmaz.kotlinsimpleweather.weather_model

import androidx.room.Entity



data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)