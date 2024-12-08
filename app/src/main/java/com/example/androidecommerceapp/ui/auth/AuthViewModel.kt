package com.example.androidecommerceapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.dataModel.ToastEvent
import com.example.androidecommerceapp.repository.AuthRepository
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.utils.ToastTypeM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    // StateFlow to manage login and signup states
    private val _authState = MutableStateFlow<ResultState<Any>>(ResultState.Loading)
    val authState: StateFlow<ResultState<Any>> = _authState

    // SharedFlow to manage toast events
    private val _toastEvent = MutableSharedFlow<ToastEvent>()
    val toastEvent: SharedFlow<ToastEvent> = _toastEvent

    // Login method
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                _authState.value = result
                when (result) {
                    is ResultState.Success -> {
                        // Trigger success toast event
                        sendToastEvent("Login Successful!", ToastTypeM.SUCCESS)
                    }

                    is ResultState.Error -> {
                        // Trigger error toast event
                        sendToastEvent(
                            result.exception.message ?: "Login Failed",
                            ToastTypeM.ERROR
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    // Signup method
    fun signup(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signup(email, password).collect { result ->
                _authState.value = result
            }
        }
    }

    // Function to send toast events
    private fun sendToastEvent(message: String, type: ToastTypeM) {
        viewModelScope.launch {
            _toastEvent.emit(ToastEvent(message, type))
        }
    }
}
