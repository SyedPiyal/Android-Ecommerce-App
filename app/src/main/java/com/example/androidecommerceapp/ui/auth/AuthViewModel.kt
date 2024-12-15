package com.example.androidecommerceapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.dataModel.ToastEvent
import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.repository.AuthRepository
import com.example.androidecommerceapp.repository.UserRepository
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
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,private val userRepository: UserRepository) : ViewModel() {

    // StateFlow to manage login and signup states
    private val _authState = MutableStateFlow<ResultState<Any>>(ResultState.Loading)
    val authState: StateFlow<ResultState<Any>> = _authState

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    data class LoginState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isSuccess: Boolean = false // Track if the login was successful
    )

    private val _signupState = MutableStateFlow(SignupState())
    val signupState: StateFlow<SignupState> = _signupState

    data class SignupState(val isSuccess: Boolean = false, val isLoading: Boolean = false, val errorMessage: String? = null)

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

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                userRepository.loginUser(email, password).collect { user ->
                    if (user != null) {
                        _loginState.value = LoginState(isSuccess = true,isLoading = false)
                    } else {
                        _loginState.value = LoginState(isLoading = false, errorMessage = "Invalid credentials")
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _signupState.value = SignupState(isLoading = true)
            try {
                val user = User(email = email, password = password)
                userRepository.registerUser(user)
                _signupState.value = SignupState(isLoading = false)
                _signupState.value = SignupState(isSuccess = true)
            } catch (e: Exception) {
                _signupState.value = SignupState(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun checkIfUserExists(email: String) {
        viewModelScope.launch {
            userRepository.checkIfUserExists(email).collect { user ->
                if (user != null) {
                    _signupState.value = SignupState(errorMessage = "Email already exists")
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
