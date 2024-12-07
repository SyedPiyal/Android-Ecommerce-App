package com.example.androidecommerceapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.repository.AuthRepository
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    // StateFlow to manage login and signup states
    private val _authState = MutableStateFlow<ResultState<Any>>(ResultState.Loading)
    val authState: StateFlow<ResultState<Any>> = _authState

    // Login method
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                _authState.value = result
            }
        }
    }

    // Signup method
    fun signup(email: String, password: String, name: String) {
        viewModelScope.launch {
            authRepository.signup(email, password, name).collect { result ->
                _authState.value = result
            }
        }
    }
}
