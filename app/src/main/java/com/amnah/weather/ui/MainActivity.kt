package com.amnah.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amnah.weather.R
import com.amnah.weather.data.model.Current
import com.amnah.weather.data.model.WeatherResponse
import com.amnah.weather.data.model.search.SearchWeatherResponse
import com.amnah.weather.data.network.ApiClient
import com.amnah.weather.data.network.Location
import com.amnah.weather.databinding.ActivityMainBinding
import com.amnah.weather.util.Constants
import com.amnah.weather.util.CustomImage
import com.amnah.weather.util.DateFormatWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreenTheme)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(requireNotNull(_binding.root))
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        Location(fusedLocationProviderClient, this).getCurrentLocation()
        makeRequestForWeatherApi()

    }

    private fun makeRequestForWeatherApi(
        latitude: String = Constants.DEFAULT_LATITUDE,
        longitude: String = Constants.DEFAULT_LONGITUDE
    ) {
        apiClient = ApiClient(latitude, longitude)

        apiClient.client.newCall(apiClient.oneCallRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("onFailure", e.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                response.body()?.string()?.let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)
                    val current = result.current
                    runOnUiThread {
                        _binding.apply {
                            searching.setOnClickListener {
                                getSearching(editSearch.text.toString())
                            }
                        }

                        current?.let { showData(result, it) }

                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun showData(
        result: WeatherResponse,
        current: Current
    ) {

        _binding.apply {
            current.weather?.joinToString { it?.id.toString() }
                ?.let { CustomImage.getImage(it) }
                ?.also {
                    currentIconWeather.setImageResource(it)
                }
            temperature.text = "${current.temp?.toInt()}ْْ C"

            currentStateWeather.text = current.weather?.joinToString {
                it?.description.toString()
            }

            date.text = current.dt?.let {
                DateFormatWeather.getDateTime(
                    it,
                    "EEEE.d MMMM"
                )
            }

            tempWind.text = "${current.windSpeed?.toInt()}ْْkm/h"

            tempHumidity.text = "${current.humidity}ْْ%"

            tempClouds.text = "${current.clouds}%"

            dailyWeatherStateRecycler.adapter = result.daily?.let {
                DailyWeatherAdapter(
                    it
                )
            }
        }

    }

    fun getSearching(name: String) {
        apiClient = ApiClient(name = name)

        apiClient.client.newCall(apiClient.weatherRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("lllllllllllll", e.message.toString())

            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.string()?.let { jsonString ->
                    val result = Gson().fromJson(jsonString, SearchWeatherResponse::class.java)

                    runOnUiThread {
                        makeRequestForWeatherApi(
                            result?.coord?.lat.toString(),
                            result?.coord?.lon.toString()
                        )
                    }
                }
            }

        })

    }

}