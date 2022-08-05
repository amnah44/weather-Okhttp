package com.amnah.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amnah.weather.R
import com.amnah.weather.databinding.ActivityMainBinding
import com.amnah.weather.model.WeatherResponse
import com.amnah.weather.util.Constants
import com.amnah.weather.util.CustomImage
import com.amnah.weather.util.DateFormatWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(requireNotNull(_binding.root))
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()


    }

    private fun makeRequestForWeatherApi(
        latitude: String = Constants.DEFAULT_LATITUDE,
        longitude: String = Constants.DEFAULT_LONGITUDE
    ) {
        val urlWeather = HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
            .addPathSegments(Constants.PATH_SEGMENTS)
            .addQueryParameter(Constants.UNITS, Constants.METRIC)
            .addQueryParameter(Constants.EXCLUDE, Constants.MINUTELY)
            .addQueryParameter(Constants.APP_ID, Constants.API_KEY)
            .addQueryParameter(Constants.LATITUDE, latitude)
            .addQueryParameter(Constants.LONGITUDE, longitude)
            .build()

        val request = Request.Builder()
            .url(urlWeather)
            .build()

        client.newCall(request).enqueue(object : Callback {
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
                                Toast.makeText(this@MainActivity, editSearch.text.toString(), LENGTH_SHORT).show()
                            }

                            current?.weather?.joinToString { it?.id.toString() }
                                ?.let { CustomImage.getImage(it) }
                                ?.also {
                                    currentIconWeather.setImageResource(it)
                                }
                            temperature.text = "${current?.temp?.toInt()}ْْ C"

                            currentStateWeather.text = current?.weather?.joinToString {
                                it?.description.toString()
                            }

                            date.text = current?.dt?.let {
                                DateFormatWeather.getDateTime(
                                    it,
                                    "EEEE.d MMMM"
                                )
                            }

                            tempWind.text = "${current?.windSpeed?.toInt()}ْْkm/h"

                            tempHumidity.text = "${current?.humidity}ْْ%"

                            tempClouds.text = "${current?.clouds}%"

                            dailyWeatherStateRecycler.adapter = result.daily?.let {
                                DailyWeatherAdapter(
                                    it
                                )
                            }
                        }

                    }
                }
            }
        })
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                // for get latitude and longitude
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        Toast.makeText(this, "Null Received", LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Get Success", LENGTH_SHORT).show()
                        makeRequestForWeatherApi(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                // for setting
                Toast.makeText(this, "Turn on Location", LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            //request permission
            requestPermissions()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Denied", LENGTH_SHORT).show()

            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    fun View.visibleLayout() {
        this.visibility = View.VISIBLE
    }
}