package com.emirhanuyunmaz.kotlinsimpleweather.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emirhanuyunmaz.kotlinsimpleweather.API_KEY
import com.emirhanuyunmaz.kotlinsimpleweather.BASE_URL
import com.emirhanuyunmaz.kotlinsimpleweather.R
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabase
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import com.emirhanuyunmaz.kotlinsimpleweather.service.WeatherApiService
import com.emirhanuyunmaz.kotlinsimpleweather.weather_model.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectCityWeatherViewModel(var myapplication: Application): AndroidViewModel(myapplication) {

    private lateinit var weatherService:WeatherApiService
    var cityWeather=MutableLiveData<WeatherDatabaseModel>()
    var error=MutableLiveData<Boolean>()
    var loading=MutableLiveData<Boolean>()
    var images_description=MutableLiveData<String>()
    var back_images=MutableLiveData<Int>()
    var mod_images=MutableLiveData<Int>()


    fun getDataFromApi(lat:Double,lon:Double){
        error.value=false
        loading.value=true
        weatherService=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(WeatherApiService::class.java)

        weatherService.getWeather(lat,lon, API_KEY).enqueue(object :Callback<WeatherModel>{
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {

                if (response.isSuccessful){
                    response.body()?.let {
                        images_description.value=it.weather[0].description
                        imagesSelect(it.weather[0].description)
                        var newWeatherCity=WeatherDatabaseModel(it.name,it.main.temp_max.toString(),it.main.temp_min.toString(),it.main.humidity.toString(),it.main.temp.toString(),it.main.pressure.toString(),it.wind.speed.toString(),it.weather[0].description.toString(),it.sys.sunrise.toString(),it.coord.lat.toString(),it.coord.lon.toString())
                        //database saved
                        cityWeather.value=newWeatherCity
                        CoroutineScope(Dispatchers.IO).launch {
                            val database=WeatherDatabase.invoke(myapplication.applicationContext).getDao()
                            database.update(newWeatherCity)
                            withContext(Dispatchers.Main){
                                cityWeather.value=newWeatherCity
                            }
                        }
                        loading.value=false
                    }
                }

            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                error.value=true
            }
        })
    }


    fun getDataFromSQLite(uuid: Int){
        loading.value=true
        error.value=false

        CoroutineScope(Dispatchers.IO).launch {

            val oldWeatherCity=WeatherDatabase.invoke(myapplication.applicationContext).getDao().selectCity(uuid)
            withContext(Dispatchers.Main){
                cityWeather.value=oldWeatherCity
                imagesSelect(oldWeatherCity.description)
            }

        }


    }

    private fun  imagesSelect(destcription: String){
        if (destcription.equals("clear sky")){
            back_images.value= R.drawable.back_clear_sky
            mod_images.value=R.drawable.sunny

        }
        if (destcription.equals("few clouds")){

            back_images.value=R.drawable.back_few_clouds
            mod_images.value=R.drawable.few_clouds

        }
        if(destcription.equals("scattered clouds")){

            back_images.value=R.drawable.back_scattered_clouds
            mod_images.value=R.drawable.scattered_clouds

        }
        if(destcription.equals("broken clouds")){

            back_images.value=R.drawable.back_broken_clouds
            mod_images.value=R.drawable.broken_clouds

        }
        if(destcription.equals("shower rain")){

            back_images.value=R.drawable.shower_rain
            mod_images.value=R.drawable.back_shower_rain

        }
        if(destcription.equals("rain")){

            back_images.value=R.drawable.back_rain
            mod_images.value=R.drawable.back_rain

        }
        if (destcription.equals("thunderstorm")){

            back_images.value=R.drawable.back_thunderstorm
            mod_images.value=R.drawable.thunderstorm

        }
        if (destcription.equals("snow")){

            back_images.value=R.drawable.back_snow
            mod_images.value=R.drawable.snow
        }
        if (destcription.equals("mist")){

            back_images.value=R.drawable.back_mist
            mod_images.value=R.drawable.mist
        }

    }



}