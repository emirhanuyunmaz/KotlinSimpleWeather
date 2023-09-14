package com.emirhanuyunmaz.kotlinsimpleweather.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabase
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import com.emirhanuyunmaz.kotlinsimpleweather.weather_model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCityMapsViewModel(var myapplication: Application): AndroidViewModel(myapplication) {

    fun addCity(weatherModel: WeatherModel){
            addCitySQLite(weatherModel)
    }


    private fun addCitySQLite(weatherModel: WeatherModel){

        val db=WeatherDatabase.invoke(myapplication.applicationContext).getDao()

        CoroutineScope(Dispatchers.IO).launch {
            var newCityWeather=WeatherDatabaseModel(weatherModel.name,weatherModel.main.temp_max.toString(),weatherModel.main.temp_min.toString(),weatherModel.main.humidity.toString(),weatherModel.main.temp.toString(),weatherModel.main.pressure.toString(),weatherModel.wind.speed.toString(),weatherModel.weather[0].description,weatherModel.sys.sunrise.toString(),weatherModel.coord.lat.toString(),weatherModel.coord.lon.toString())
            db.insert(newCityWeather)
            withContext(Dispatchers.Main){

            }
        }

    }

}