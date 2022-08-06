package com.amnah.weather.model.search


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("humidity")
    var humidity: Int?,
    @SerializedName("pressure")
    var pressure: Int?,
    @SerializedName("temp")
    var temp: Double?,
    @SerializedName("temp_max")
    var tempMax: Double?,
    @SerializedName("temp_min")
    var tempMin: Double?
)