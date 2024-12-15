package com.example.androidecommerceapp.dataModel

data class LoginState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false // Track if the login was successful
)