package com.amnah.weather.data.model.search


import com.google.gson.annotations.SerializedName

data class SearchWeatherResponse(
    @SerializedName("base")
    var base: String?,
    @SerializedName("clouds")
    var clouds: Clouds?,
    @SerializedName("cod")
    var cod: Int?,
    @SerializedName("coord")
    var coord: Coord?,
    @SerializedName("dt")
    var dt: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("main")
    var main: Main?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("sys")
    var sys: Sys?,
    @SerializedName("timezone")
    var timezone: Int?,
    @SerializedName("visibility")
    var visibility: Int?,
    @SerializedName("weather")
    var weather: List<Weather?>?,
    @SerializedName("wind")
    var wind: Wind?
)