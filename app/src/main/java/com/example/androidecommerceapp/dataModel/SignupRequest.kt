package com.example.androidecommerceapp.dataModel

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String
)

data class SignupResponse(
    val id: Int,
    val email: String,
    val name: String,
    val token: String
)
