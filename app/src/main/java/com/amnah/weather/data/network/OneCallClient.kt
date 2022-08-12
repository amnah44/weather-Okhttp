package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.data.model.onecall.WeatherResponse
import com.amnah.weather.util.Constants
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class OneCallClient(
    val latitude: String = Constants.DEFAULT_LATITUDE,
    val longitude: String = Constants.DEFAULT_LONGITUDE,
) {
    private val client = OkHttpClient()

    fun getOneCallRequest(
        getCurrentWeatherData: (response: WeatherResponse) -> Unit
    ) {

        val response = Request.Builder()
            .url(getOneCallUrl()).build()

        client.newCall(response).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(Constants.ERROR_MESSAGE, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, WeatherResponse::class.java)

                    getCurrentWeatherData(result)
                }
            }

        })
    }

    fun getOneCallUrl(): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
            .addPathSegments(Constants.PATH_SEGMENTS)
            .addQueryParameter(Constants.UNITS, Constants.METRIC)
            .addQueryParameter(Constants.EXCLUDE, Constants.MINUTELY)
            .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
            .addQueryParameter(Constants.LATITUDE, latitude)
            .addQueryParameter(Constants.LONGITUDE, longitude)
            .build()
    }


}