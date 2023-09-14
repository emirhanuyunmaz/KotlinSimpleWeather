package com.emirhanuyunmaz.kotlinsimpleweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emirhanuyunmaz.kotlinsimpleweather.DATABASE_NAME

@Database(entities = [WeatherDatabaseModel::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getDao(): WeatherDao

    companion object{
        @Volatile private var instance:WeatherDatabase? = null
        private val lock=Any()

        operator fun invoke(context: Context)= instance ?: synchronized(lock){
            instance ?: WeatherDatabase.makeDatabase(context).also{
                instance=it
            }

        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(context,WeatherDatabase::class.java,
            DATABASE_NAME).build()
    }
}