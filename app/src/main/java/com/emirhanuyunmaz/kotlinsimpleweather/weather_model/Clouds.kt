package com.emirhanuyunmaz.kotlinsimpleweather.weather_model

import androidx.room.Entity

@Entity
data class Clouds(
    val all: Int
)