package com.example.androidecommerceapp.ui.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val _updateState = MutableStateFlow(UpdateState())
    val updateState: StateFlow<UpdateState> = _updateState

    data class UpdateState(val isLoading: Boolean = false, val isSuccess: Boolean = false, val errorMessage: String? = null)

    // Fetch the current logged-in user data
    fun fetchUserData(email: String) {
        viewModelScope.launch {
            userRepository.checkIfUserExists(email).collect { user ->
                _userData.value = user
            }
        }
    }

    // Update user data
    fun updateUserData(user: User) {
        viewModelScope.launch {
            _updateState.value = UpdateState(isLoading = true)
            try {
                userRepository.registerUser(user) // You can call the same function used for registration or create an update function in your repository.
                _updateState.value = UpdateState(isSuccess = true, isLoading = false)
            } catch (e: Exception) {
                _updateState.value = UpdateState(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
