package com.emirhanuyunmaz.kotlinsimpleweather.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabase
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherListViewModel(var myapplication: Application):AndroidViewModel(myapplication) {

    var cityList = MutableLiveData<List<WeatherDatabaseModel>>()

    fun getCityList(){
        val db=WeatherDatabase.invoke(myapplication.applicationContext).getDao()
        CoroutineScope(Dispatchers.IO).launch {
            var getAll=db.getAllData()
            withContext(Dispatchers.Main){
                cityList.value=getAll
            }

        }

    }
}