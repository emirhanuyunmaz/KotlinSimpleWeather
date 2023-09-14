package com.emirhanuyunmaz.kotlinsimpleweather.service

import com.emirhanuyunmaz.kotlinsimpleweather.weather_model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("/data/2.5/weather?&units=metric")
    fun getWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appid:String
    ):Call<WeatherModel>


    @GET("/img/wn/")
    fun weatherImages(@Query("") imageName:String,@Query("") zoom:String):Call<String>

}