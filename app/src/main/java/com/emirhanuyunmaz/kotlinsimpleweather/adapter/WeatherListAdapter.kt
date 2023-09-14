package com.emirhanuyunmaz.kotlinsimpleweather.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.emirhanuyunmaz.kotlinsimpleweather.R
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabase
import com.emirhanuyunmaz.kotlinsimpleweather.database.WeatherDatabaseModel
import com.emirhanuyunmaz.kotlinsimpleweather.databinding.RvWeatherRowBinding
import com.emirhanuyunmaz.kotlinsimpleweather.view.SelectCityWeatherActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherListAdapter(var weatherDatabaseModelList:ArrayList<WeatherDatabaseModel>,var context: Context) :RecyclerView.Adapter<WeatherListAdapter.WeatherlistVH>() {
    var back_images:Int = R.drawable.back_clear_sky
    var mod_images: Int = R.drawable.sunny

    inner class WeatherlistVH(var binding:RvWeatherRowBinding) :RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherlistVH {
        val weatherListVH=RvWeatherRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeatherlistVH(weatherListVH)
    }

    override fun getItemCount(): Int {
        return weatherDatabaseModelList.size
    }

    override fun onBindViewHolder(holder: WeatherlistVH, position: Int) {
        imagesSelect(weatherDatabaseModelList[position].description)
        holder.binding.tvRVCityName.text=weatherDatabaseModelList[position].name
        holder.binding.tvRVTemp.text=weatherDatabaseModelList[position].temp+"°C"
        Picasso.get().load(back_images).into(holder.binding.ivRVBackground)
        Picasso.get().load(mod_images).into(holder.binding.ivRVMod)

        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context,SelectCityWeatherActivity::class.java)
            intent.putExtra("uuid",weatherDatabaseModelList[position].uuid)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.rowLayout.setOnLongClickListener {
            println("long click")
            popUpMenu(holder.binding.rowLayout,weatherDatabaseModelList[position])
            true
        }
    }

    private fun  imagesSelect(destcription: String){
        if (destcription.equals("clear sky")){
            back_images= R.drawable.back_clear_sky
            mod_images= R.drawable.sunny

        }
        if (destcription.equals("few clouds")){

            back_images= R.drawable.back_few_clouds
            mod_images= R.drawable.few_clouds

        }
        if(destcription.equals("scattered clouds")){

            back_images= R.drawable.back_scattered_clouds
            mod_images= R.drawable.scattered_clouds

        }
        if(destcription.equals("broken clouds")){

            back_images= R.drawable.back_broken_clouds
            mod_images= R.drawable.broken_clouds

        }
        if(destcription.equals("shower rain")){

            back_images= R.drawable.shower_rain
            mod_images= R.drawable.back_shower_rain

        }
        if(destcription.equals("rain")){

            back_images= R.drawable.back_rain
            mod_images= R.drawable.back_rain

        }
        if (destcription.equals("thunderstorm")){

            back_images= R.drawable.back_thunderstorm
            mod_images= R.drawable.thunderstorm

        }
        if (destcription.equals("snow")){

            back_images= R.drawable.back_snow
            mod_images= R.drawable.snow
        }
        if (destcription.equals("mist")){

            back_images= R.drawable.back_mist
            mod_images= R.drawable.mist
        }
    }

    fun refreshData(newweatherDatabaseModelList: ArrayList<WeatherDatabaseModel>){
        weatherDatabaseModelList.clear()
        weatherDatabaseModelList.addAll(newweatherDatabaseModelList)
        notifyDataSetChanged()
    }

    private fun popUpMenu(view: View,weatherDatabaseModel: WeatherDatabaseModel){
        var popup=PopupMenu(context,view)
        var inflater=popup.menuInflater
        inflater.inflate(R.menu.delete_city,popup.menu)
        popup.show()

        popup.setOnMenuItemClickListener {
            if(it.itemId==R.id.menuDelete){
                weatherDatabaseModelList.clear()
                val database=WeatherDatabase.invoke(context).getDao()
                CoroutineScope(Dispatchers.IO).launch {
                    database.delete(weatherDatabaseModel)
                    val newData= ArrayList(database.getAllData())
                    withContext(Dispatchers.Main){
                        weatherDatabaseModelList.addAll(newData)
                        refreshData(newData)
                    }

                }
            }
            true
        }


    }

}