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
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amnah.weather.R
import com.amnah.weather.data.model.onecall.Current
import com.amnah.weather.data.network.OneCallClient
import com.amnah.weather.data.network.WeatherSearchClient
import com.amnah.weather.databinding.ActivityMainBinding
import com.amnah.weather.util.Constants
import com.amnah.weather.util.CustomImage
import com.amnah.weather.util.DateFormatWeather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreenTheme)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(requireNotNull(_binding.root))

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()

        _binding.apply {
            searching.setOnClickListener {
                searchWeather(_binding.editSearch.text.toString())
            }
        }

    }


    private fun makeRequestForCurrentWeather(
        latitude: String = Constants.DEFAULT_LATITUDE,
        longitude: String = Constants.DEFAULT_LONGITUDE
    ) {

        OneCallClient(latitude, longitude).getOneCallRequest() { response ->
            val current = response.current

            runOnUiThread {
                _binding.apply {
                    current?.let { showData(it) }

                    Log.i("nnnnnnnnnnnnnnnnn", response.timezone.toString())

                    dailyWeatherStateRecycler.adapter = response.daily?.let {
                        DailyWeatherAdapter(
                            it
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showData(
        current: Current
    ) {

        _binding.apply {
            current.weather?.joinToString { it?.id.toString() }
                ?.let { CustomImage.getImage(it) }
                ?.also {
                    currentIconWeather.setImageResource(it)
                }
            temperature.text = "${current.temp?.toInt()}${Constants.C_temperature}"

            currentStateWeather.text = current.weather?.joinToString {
                it?.description.toString()
            }

            date.text = current.dt?.let {
                DateFormatWeather.getDateTime(
                    it,
                    "EEEE.d MMMM"
                )
            }

            tempWind.text = "${current.windSpeed?.toInt()}ْْ${Constants.WIND_SPEED_SCALE}"

            tempHumidity.text = "${current.humidity}ْْ${Constants.MOOD}"

            tempClouds.text = "${current.clouds}${Constants.MOOD}"
        }
    }


    private fun searchWeather(name: String) {
        WeatherSearchClient(name).getSearchWeather() { response ->
            val coord = response.coord
            runOnUiThread {
                makeRequestForCurrentWeather(
                    latitude = coord?.lat.toString(),
                    longitude = coord?.lon.toString(),
                )
            }
        }
    }


    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                // for get latitude and longitude
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        Toast.makeText(this, Constants.NULL_RECEIVED, LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, Constants.GET_SUCCESS, LENGTH_SHORT).show()

                        makeRequestForCurrentWeather(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                // for setting
                Toast.makeText(this, Constants.TURN_ON_LOCATION, LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            //oneCallRequest permission
            requestPermissions()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
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
                Toast.makeText(this, Constants.GRANTED, LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, Constants.DENIED, LENGTH_SHORT).show()

            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = Constants.ONE_HUNDRED
    }
}