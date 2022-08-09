package com.amnah.weather.util

import com.amnah.weather.R

object CustomImage {

    fun getImage(id: String): Int {
        val imageId: Map<String, Int> = mapOf(
            "200" to R.drawable.thunder,
            "800" to R.drawable.sun,
            "801" to R.drawable.cloudy,
            "804" to R.drawable.fog,
            "500" to R.drawable.fog,
        )
        return imageId.getOrDefault(id, R.drawable.fog)
    }
}