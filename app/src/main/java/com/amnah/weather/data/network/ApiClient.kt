package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.util.Constants
import okhttp3.HttpUrl

class ApiClient(
    val latitude: String = Constants.DEFAULT_LATITUDE,
    val longitude: String = Constants.DEFAULT_LONGITUDE,
    val cityName: String = Constants.DEFAULT_CITY_NAME
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