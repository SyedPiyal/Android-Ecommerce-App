package com.example.androidecommerceapp.dataModel

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val email: String,
    val token: String
)

