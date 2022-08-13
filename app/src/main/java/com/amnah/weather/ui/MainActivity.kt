package com.amnah.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amnah.weather.R
import com.amnah.weather.data.model.onecall.Current
import com.amnah.weather.data.network.OneCallClient
import com.amnah.weather.data.network.Status
import com.amnah.weather.data.network.WeatherSearchClient
import com.amnah.weather.databinding.ActivityMainBinding
import com.amnah.weather.util.Constants
import com.amnah.weather.util.CustomImage
import com.amnah.weather.util.DateFormatWeather
import com.amnah.weather.util.addDisposable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val compositeDisposable = CompositeDisposable()

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

        val observable = Observable.create { emitter ->
            emitter.onNext(Status.OnLoading)
            emitter.onNext(OneCallClient(latitude, longitude).getOneCallRequest())
        }

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { response ->
                when (response) {
                    Status.OnLoading -> Toast.makeText(this, "loading", LENGTH_SHORT).show()
                    is Status.OnSuccess -> {
                        response.data?.current?.let { showData(it) }
                        _binding.apply {
                            dailyWeatherStateRecycler.adapter = response.data?.daily?.let {
                                DailyWeatherAdapter(
                                    it
                                )
                            }
                        }
                    }
                    is Status.OnFailure -> Toast.makeText(this, "error", LENGTH_SHORT).show()
                }
            }, {
                Status.OnFailure(it.message)
            }
        ).addDisposable(compositeDisposable)
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

        val observable = Observable.create { emitter ->
            emitter.onNext(Status.OnLoading)
            emitter.onNext(WeatherSearchClient(name).getSearchWeather())
        }

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            { response ->
                when (response) {
                    Status.OnLoading -> Toast.makeText(this, "loading", LENGTH_SHORT).show()
                    is Status.OnSuccess -> Toast.makeText(this, "success", LENGTH_SHORT).show()
                    is Status.OnFailure -> Toast.makeText(this, "error", LENGTH_SHORT).show()
                }
            },
            { error ->

                Status.OnFailure(error.message)

            }
        ).addDisposable(compositeDisposable)
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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = Constants.ONE_HUNDRED
    }
}