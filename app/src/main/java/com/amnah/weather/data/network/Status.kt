package com.amnah.weather.data.network

sealed class Status<out T>{
    object OnLoading: Status<Nothing>()
    data class OnSuccess<T>(val data: T?): Status<T>()
    data class OnError<T>(val message: String?): Status<T>()
}
