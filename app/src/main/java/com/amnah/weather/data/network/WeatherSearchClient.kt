package com.amnah.weather.data.network

import com.amnah.weather.data.model.search.SearchWeatherResponse
import com.amnah.weather.util.Constants
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class WeatherSearchClient(
    private val cityName: String = Constants.DEFAULT_CITY_NAME
) {

    private val client = OkHttpClient()

    fun getSearchWeather(): Status<SearchWeatherResponse?> {
        val request = Request.Builder()
            .url(WeatherSearchClient(cityName = cityName).getWeatherUrl()).build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val result = Gson().fromJson(response.body?.string(), SearchWeatherResponse::class.java)
            Status.OnSuccess(result)
        } else {
            Status.OnFailure(response.message)
        }
    }

    private fun getWeatherUrl(
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