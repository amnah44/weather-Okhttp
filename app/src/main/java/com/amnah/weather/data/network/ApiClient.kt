package com.amnah.weather.data.network

import com.amnah.weather.util.Constants
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiClient(
    latitude: String = "33.3152",
    longitude: String = "44.3661",
    name: String = "Baghdad"
) {
    val client = OkHttpClient()
    private val oneCallUrl = HttpUrl.Builder()
        .scheme(Constants.SCHEME)
        .host(Constants.BASE_URL)
        .addPathSegments(Constants.PATH_SEGMENTS)
        .addQueryParameter(Constants.UNITS, Constants.METRIC)
        .addQueryParameter(Constants.EXCLUDE, Constants.MINUTELY)
        .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
        .addQueryParameter(Constants.LATITUDE, latitude)
        .addQueryParameter(Constants.LONGITUDE, longitude)
        .build()

    val oneCallRequest: Request = Request.Builder()
        .url(oneCallUrl)
        .build()

    private val weatherUrl = HttpUrl.Builder()
        .scheme(Constants.SCHEME)
        .host(Constants.BASE_URL)
        .addPathSegments(Constants.PATH_SEGMENTS_BY_NAME)
        .addQueryParameter(Constants.Q, name)
        .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
        .build()

    val weatherRequest: Request = Request.Builder()
        .url(weatherUrl)
        .build()

}