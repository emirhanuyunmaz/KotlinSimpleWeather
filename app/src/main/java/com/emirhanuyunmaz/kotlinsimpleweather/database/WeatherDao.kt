package com.emirhanuyunmaz.kotlinsimpleweather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather_Table")
    fun getAllData():List<WeatherDatabaseModel>

    @Delete
    fun delete(weatherDatabaseModel: WeatherDatabaseModel)

    @Insert
    fun insert(vararg weatherDatabaseModel: WeatherDatabaseModel)

    @Query("SELECT * FROM Weather_Table WHERE uuid=:uuid")
    fun selectCity(uuid:Int):WeatherDatabaseModel

    @Query("DELETE FROM Weather_Table")
    fun deleteAll()

    @Update
    fun update(weatherDatabaseModel: WeatherDatabaseModel)



}