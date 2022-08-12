package com.amnah.weather.data.network

import com.amnah.weather.data.model.onecall.WeatherResponse
import com.amnah.weather.util.Constants
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request


class OneCallClient(
    private val latitude: String = Constants.DEFAULT_LATITUDE,
    private val longitude: String = Constants.DEFAULT_LONGITUDE,
) {
    private val client = OkHttpClient()

    fun getOneCallRequest(): Status<WeatherResponse?> {
        val request = Request.Builder().url(getOneCallUrl(latitude, longitude)).build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val result = Gson().fromJson(response.body?.string(), WeatherResponse::class.java)
            Status.OnSuccess(result)
        } else {
            Status.OnError(response.message)
        }
    }

    private fun getOneCallUrl(
        lat: String,
        lon: String,
    ): HttpUrl {
        return HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
            .addPathSegments(Constants.PATH_SEGMENTS)
            .addQueryParameter(Constants.UNITS, Constants.METRIC)
            .addQueryParameter(Constants.EXCLUDE, Constants.MINUTELY)
            .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
            .addQueryParameter(Constants.LATITUDE, lat)
            .addQueryParameter(Constants.LONGITUDE, lon)
            .build()
    }

}