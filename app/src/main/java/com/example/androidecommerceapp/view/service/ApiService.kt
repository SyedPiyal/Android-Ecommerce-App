package com.example.androidecommerceapp.view.service

import com.example.androidecommerceapp.view.dataModel.LoginRequest
import com.example.androidecommerceapp.view.dataModel.LoginResponse
import com.example.androidecommerceapp.view.dataModel.SignupRequest
import com.example.androidecommerceapp.view.dataModel.SignupResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    // Login API endpoint
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Signup API endpoint
    @POST("register")
    suspend fun signup(@Body request: SignupRequest): SignupResponse


}