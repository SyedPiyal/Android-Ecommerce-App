package com.example.androidecommerceapp.view.auth.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.view.dataModel.LoginState
import com.example.androidecommerceapp.view.dataModel.SignupState
import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.view.auth.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState


    private val _signupState = MutableStateFlow(SignupState())
    val signupState: StateFlow<SignupState> = _signupState


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState(isLoading = true)
            try {
                userRepository.loginUser(email, password).collect { user ->
                    if (user != null) {
                        _loginState.value = LoginState(isSuccess = true, isLoading = false)
                    } else {
                        _loginState.value =
                            LoginState(isLoading = false, errorMessage = "Invalid credentials")
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


}
