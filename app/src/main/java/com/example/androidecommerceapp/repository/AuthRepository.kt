package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.dataModel.LoginRequest
import com.example.androidecommerceapp.dataModel.LoginResponse
import com.example.androidecommerceapp.dataModel.SignupRequest
import com.example.androidecommerceapp.dataModel.SignupResponse
import com.example.androidecommerceapp.service.ApiService
import com.example.androidecommerceapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    // A generic function to handle API calls for different requests
    suspend fun <T> makeApiCall(
        apiCall: suspend () -> T
    ): Flow<ResultState<T>> = flow {
        try {
            emit(ResultState.Loading)
            val response = apiCall()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    // Login function
    suspend fun login(email: String, password: String): Flow<ResultState<LoginResponse>> {
        return makeApiCall {
            apiService.login(LoginRequest(email, password))
        }
    }

    // Signup function
    suspend fun signup(email: String, password: String, name: String): Flow<ResultState<SignupResponse>> {
        return makeApiCall {
            apiService.signup(SignupRequest(email, password, name))
        }
    }
}
