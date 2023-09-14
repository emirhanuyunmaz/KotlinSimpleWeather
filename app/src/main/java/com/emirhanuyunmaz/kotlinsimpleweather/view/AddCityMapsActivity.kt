package com.emirhanuyunmaz.kotlinsimpleweather.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.emirhanuyunmaz.kotlinsimpleweather.API_KEY
import com.emirhanuyunmaz.kotlinsimpleweather.BASE_URL
import com.emirhanuyunmaz.kotlinsimpleweather.R

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.emirhanuyunmaz.kotlinsimpleweather.databinding.ActivityAddCityMapsBinding
import com.emirhanuyunmaz.kotlinsimpleweather.service.WeatherApiService
import com.emirhanuyunmaz.kotlinsimpleweather.view_model.AddCityMapsViewModel
import com.emirhanuyunmaz.kotlinsimpleweather.weather_model.WeatherModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddCityMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddCityMapsBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var viewModel: AddCityMapsViewModel
    private var markerLatLng : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddCityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel=ViewModelProvider(this).get(AddCityMapsViewModel::class.java)
        binding.tvAddCityError.visibility=View.GONE
        registerPermission()


    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        var local=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var inter=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //************Location Permission *************************
        if (local.isLocationEnabled==true && inter.activeNetwork!=null){
            when {
                ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted
                    mMap.isMyLocationEnabled=true

                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    //permission needed
                    //Snackbar
                    Snackbar.make(binding.root,"Permission needed",Snackbar.LENGTH_INDEFINITE).setAction("Allow",){
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }

                }
                else -> {
                    // You can directly ask for the permission.
                    //permission needed
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
        else{
            binding.tvAddCityError.visibility=View.VISIBLE
            binding.buttonSaveLocation.visibility=View.GONE
            mMap.isBuildingsEnabled=false
        }


        //********


        //*************Long Click *****************
        mMap.setOnMapLongClickListener {latlng->
            mMap.clear()
            markerLatLng=latlng
            mMap.addMarker(MarkerOptions().position(latlng))
        }

    }

    fun save_OnClick(view: View){
        if (markerLatLng!=null){
            weatherGetData(lat = markerLatLng!!.latitude, lon =markerLatLng!!.longitude)

        }
        else{
            Toast.makeText(this@AddCityMapsActivity, "Please selected location", Toast.LENGTH_SHORT).show()
        }

    }

    private fun registerPermission(){
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted
                    if(ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mMap.isMyLocationEnabled=true
                    }
                } else {
                    //Permission needed
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
    }

    private fun weatherGetData(lat:Double,lon: Double) {

        val retrofit= Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(WeatherApiService::class.java)

        val service=retrofit.getWeather(lat,lon,
            API_KEY
        ).enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                println(response.isSuccessful)
                if(response.isSuccessful){
                    response?.body().let {body->
                        viewModel.addCity(body!!)
                        Toast.makeText(this@AddCityMapsActivity, body.name, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                println("ERROR")
            }
        })
    }

}