package com.amnah.weather.util

import com.amnah.weather.R

object CustomImage {

    fun getImage(id: String) : Int{
        return when (id) {
            "200" -> {
                R.drawable.thunder
            }
            "800" -> {
                R.drawable.sun
            }
            "801", "802", "803" -> {
                R.drawable.cloudy
            }
            "804" -> {
                R.drawable.fog
            }
            "500" -> {
                R.drawable.fog
            }
            else -> {
                R.drawable.fog
            }
        }
    }
}