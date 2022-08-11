package com.amnah.weather.data.network

import android.util.Log
import com.amnah.weather.data.model.onecall.WeatherResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ClientOkhttp {
    val client = OkHttpClient()

    fun getOneCallRequest(getCurrentWeatherData: (result: WeatherResponse) -> Unit) {

        val response = Request.Builder()
            .url(ApiClient().getOneCallUrl())
            .build()
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

//    fun getWeatherRequest(): Request {
//        return Request.Builder()
//            .url(apiClient.getWeatherUrl())
//            .build()
//    }

}