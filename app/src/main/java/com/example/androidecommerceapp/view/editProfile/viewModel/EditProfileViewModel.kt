package com.example.androidecommerceapp.view.editProfile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.view.auth.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _userLiveData = MutableLiveData<User?>()
    val userLiveData: LiveData<User?> get() = _userLiveData

    // Set the user data
    fun setUser(user: User) {
        Log.d("EditProfileViewModel", "Setting user: $user")
        _userLiveData.value = user
    }

    // Get the current user data
//    fun getUser(): LiveData<User> = userLiveData

    // Get user by email
    fun getUserByEmail(email: String): LiveData<User?> {
        val user = MutableLiveData<User?>()
        viewModelScope.launch {
            userRepository.checkIfUserExists(email).collect { userData ->
                user.postValue(userData)
            }
        }
        return user
    }

    fun updateUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                userRepository.getUserByEmail(email).collect { existingUser ->
                    if (existingUser != null) {
                        val updatedUser = existingUser.copy(password = password)
                        userRepository.updateUser(updatedUser)
                    } else {
                        Log.d("EditViewModel--->", "User not found")
                    }
                }
            } catch (e: Exception) {
                Log.d("EditViewModel--->", "Error $e")
            }
        }
    }

}
