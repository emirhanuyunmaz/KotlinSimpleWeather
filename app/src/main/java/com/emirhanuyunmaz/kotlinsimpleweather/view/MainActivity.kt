package com.emirhanuyunmaz.kotlinsimpleweather.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.emirhanuyunmaz.kotlinsimpleweather.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent=Intent(this@MainActivity,WeatherListActivity::class.java)
            startActivity(intent)
            finish()
        },3000)


    }


}