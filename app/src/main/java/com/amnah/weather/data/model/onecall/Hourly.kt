package com.amnah.weather.data.model.onecall


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("clouds")
    var clouds: Double?,
    @SerializedName("dew_poDouble")
    var dewPoDouble: Double?,
    @SerializedName("dt")
    var dt: Double?,
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("humidity")
    var humidity: Double?,
    @SerializedName("pop")
    var pop: Double?,
    @SerializedName("pressure")
    var pressure: Double?,
    @SerializedName("temp")
    var temp: Double?,
    @SerializedName("uvi")
    var uvi: Double?,
    @SerializedName("visibility")
    var visibility: Double?,
    @SerializedName("weather")
    var weather: List<Weather>?,
    @SerializedName("wind_deg")
    var windDeg: Double?,
    @SerializedName("wind_gust")
    var windGust: Double?,
    @SerializedName("wind_speed")
    var windSpeed: Double?
)