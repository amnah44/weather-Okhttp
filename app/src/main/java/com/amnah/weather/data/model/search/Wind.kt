package com.amnah.weather.data.model.search


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("deg")
    var deg: Int?,
    @SerializedName("speed")
    var speed: Double?
)