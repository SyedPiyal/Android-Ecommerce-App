package com.example.androidecommerceapp.service

import com.example.androidecommerceapp.dataModel.LoginRequest
import com.example.androidecommerceapp.dataModel.LoginResponse
import com.example.androidecommerceapp.dataModel.SignupRequest
import com.example.androidecommerceapp.dataModel.SignupResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    // Login API endpoint
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Signup API endpoint
    @POST("register")
    suspend fun signup(@Body request: SignupRequest): SignupResponse

    // Add other API methods as needed
}
