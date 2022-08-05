package com.amnah.weather.model


import com.google.gson.annotations.SerializedName

data class Temp(
    @SerializedName("day")
    var day: Double?,
    @SerializedName("eve")
    var eve: Double?,
    @SerializedName("max")
    var max: Double?,
    @SerializedName("min")
    var min: Double?,
    @SerializedName("morn")
    var morn: Double?,
    @SerializedName("night")
    var night: Double?
)