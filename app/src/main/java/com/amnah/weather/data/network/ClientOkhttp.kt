package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.data.model.onecall.WeatherResponse
import com.amnah.weather.data.model.search.SearchWeatherResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ClientOkhttp {
    private val client = OkHttpClient()

    fun getOneCallRequest(
        latitude: String,
        longitude: String,
        getCurrentWeatherData: (response: WeatherResponse) -> Unit
    ) {

        val response = Request.Builder()
            .url(
                ApiClient(
                    latitude = latitude,
                    longitude = longitude
                ).getOneCallUrl()
            ).build()

        client.newCall(response).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("OnFailure", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)

                    getCurrentWeatherData(result)
                }
            }

        })
    }


    fun getSearchWeather(
        name: String,
        getSearchWeather: (response: SearchWeatherResponse) -> Unit
    ) {
        val request = Request.Builder()
            .url(
                ApiClient(cityName = name).getWeatherUrl()
            ).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("OnFailure", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, SearchWeatherResponse::class.java)

                    getSearchWeather(result)
                }
            }
        })
    }

}