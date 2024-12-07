package com.example.androidecommerceapp.utils

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val exception: Exception) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}
