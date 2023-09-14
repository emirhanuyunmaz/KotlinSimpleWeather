package com.emirhanuyunmaz.kotlinsimpleweather.weather_model

import androidx.room.Entity



data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)