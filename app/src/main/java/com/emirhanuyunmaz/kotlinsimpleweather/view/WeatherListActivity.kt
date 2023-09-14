package com.emirhanuyunmaz.kotlinsimpleweather.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirhanuyunmaz.kotlinsimpleweather.R
import com.emirhanuyunmaz.kotlinsimpleweather.adapter.WeatherListAdapter
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import com.emirhanuyunmaz.kotlinsimpleweather.databinding.ActivityWeatherListBinding
import com.emirhanuyunmaz.kotlinsimpleweather.view_model.WeatherListViewModel

class WeatherListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherListBinding
    private lateinit var viewModel:WeatherListViewModel
    private lateinit var weatherArrayList: ArrayList<WeatherDatabaseModel>
    private lateinit var adapter: WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWeatherListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarWeatherList)

        weatherArrayList= ArrayList()

        viewModel=ViewModelProvider(this).get(WeatherListViewModel::class.java)

        viewModel.getCityList()

        observeData()

        adapter= WeatherListAdapter(weatherArrayList,this@WeatherListActivity)
        binding.rvWeatherList.layoutManager=LinearLayoutManager(this@WeatherListActivity)
        binding.rvWeatherList.adapter=adapter

        binding.listRefresh.setOnRefreshListener {
            viewModel.getCityList()
            binding.listRefresh.isRefreshing=false
        }

    }


    override fun onRestart() {
        super.onRestart()
        viewModel.getCityList()
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val meuInflate=MenuInflater(this@WeatherListActivity)
        meuInflate.inflate(R.menu.weather_list_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(R.id.addWeatherCity==item.itemId){
            val intent=Intent(this@WeatherListActivity,AddCityMapsActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observeData(){

        viewModel.cityList.observe(this, Observer {weatherList->
            weatherList?.let {
                adapter.refreshData(ArrayList(it))
            }
        })

    }

}