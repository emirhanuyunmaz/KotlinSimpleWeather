package com.emirhanuyunmaz.kotlinsimpleweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import com.emirhanuyunmaz.kotlinsimpleweather.databinding.ActivitySelectCityWeatherBinding
import com.emirhanuyunmaz.kotlinsimpleweather.view_model.SelectCityWeatherViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SelectCityWeatherActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySelectCityWeatherBinding
    private lateinit var viewModel:SelectCityWeatherViewModel
    private lateinit var weatherModel: WeatherDatabaseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectCityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getDataIntent=intent

        val getUUID=getDataIntent.getIntExtra("uuid",-1)

        viewModel=ViewModelProvider(this).get(SelectCityWeatherViewModel::class.java)

        viewModel.getDataFromSQLite(uuid = getUUID)
        observeLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {

            viewModel.getDataFromApi(weatherModel.lat.toDouble(),weatherModel.lng.toDouble())
            binding.swipeRefreshLayout.isRefreshing=false
        }
    }

    private fun observeLiveData(){

        viewModel.cityWeather.observe(this, Observer { weather->

            weather?.let {
                weatherModel=weather
                binding.tvMaxTemp.text= it.max_temp+"°C"
                binding.tvMinTemp.text=it.min_temp+"°C"
                binding.tvCityName.text=it.name
                binding.tvHumidity.text=it.humidity
                binding.tvTemp.text=it.temp+"°C"
                binding.tvPressure.text=it.pressure
                binding.tvWind.text=it.wind
                binding.tvMain.text=it.description
                //***
                var s= Date(it.sunrise.toLong()*1000)
                var sunrise=SimpleDateFormat(" h:mm a ", Locale.ENGLISH)
                var formatDate=sunrise.format(s)
                binding.tvSunrise.text=formatDate
            }

        })

        viewModel.back_images.observe(this, Observer {backImages->
            backImages?.let {
                Picasso.get().load(it).into(binding.ivWeatherChanges)
            }

        })
        viewModel.mod_images.observe(this, Observer {modImages->
                modImages?.let {
                    Picasso.get().load(it).into(binding.ivMod)
                }
        })

        viewModel.error.observe(this, Observer { error->

            error?.let {
                if(it){
                    binding.linearLayout.visibility= View.GONE
                    binding.ivMod.visibility=View.GONE
                    binding.tvCityName.visibility=View.GONE
                    binding.tvTemp.visibility=View.GONE
                    binding.tvMain.visibility=View.GONE
                }
                else{
                    binding.tvError.visibility=View.GONE
                }
            }

        })

    }

}