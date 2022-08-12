package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.data.model.search.SearchWeatherResponse
import com.amnah.weather.util.Constants
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class WeatherSearchClient(
    val cityName: String = Constants.DEFAULT_CITY_NAME
) {
    private val client = OkHttpClient()

    fun getSearchWeather(
        getSearchWeather: (response: SearchWeatherResponse) -> Unit
    ) {
        val request = Request.Builder()
            .url(
                WeatherSearchClient(cityName = cityName).getWeatherUrl()
            ).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(Constants.ERROR_MESSAGE, e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val result = Gson().fromJson(jsonString, SearchWeatherResponse::class.java)

                    getSearchWeather(result)
                }
            }
        })
    }

    fun getWeatherUrl(
    ): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
            .addPathSegments(Constants.PATH_SEGMENTS_BY_NAME)
            .addQueryParameter(Constants.Q, cityName)
            .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
            .build()
    }
}