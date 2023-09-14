package com.emirhanuyunmaz.kotlinsimpleweather.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weather_Table")
data class WeatherDatabaseModel(
    val name:String,
    val max_temp:String,
    val min_temp:String,
    val humidity:String,
    val temp:String,
    val pressure:String,
    val wind:String,
    val description:String,
    val sunrise:String,
    val lat:String,
    val lng:String,

){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0

}
