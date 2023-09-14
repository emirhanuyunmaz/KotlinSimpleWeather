package com.emirhanuyunmaz.kotlinsimpleweather.weather_model

import androidx.room.Entity



data class Wind(
    val deg: Int,
    val speed: Double
)