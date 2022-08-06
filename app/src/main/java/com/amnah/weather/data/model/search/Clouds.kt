package com.amnah.weather.data.model.search


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    var all: Int?
)