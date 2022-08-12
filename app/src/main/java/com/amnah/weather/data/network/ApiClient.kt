package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.util.Constants
import okhttp3.HttpUrl

class ApiClient(
    val latitude: String = "33.3152",
    val longitude: String = "44.3661",
    val cityName: String = "Baghdad"
) {

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