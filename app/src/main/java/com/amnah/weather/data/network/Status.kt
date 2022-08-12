package com.amnah.weather.data.network

sealed class Status <out T>{
    object OnLoading: Status<Nothing>()
    data class OnSuccess<T>(val data: T?): Status<T>()
    data class OnFailure(val message: String?): Status<Nothing>()

    fun toData(): T? = if (this is OnSuccess) data else null
}
