package com.example.androidecommerceapp.ui.editProfile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.database.User
import com.example.androidecommerceapp.databinding.ActivityEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "") // Get the stored email

        if (email.isNullOrEmpty()) {
            // Handle case where email is not found, maybe navigate back or show an error
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()  // Or navigate to login screen if needed
            return
        }
        // Fetch the user data
        editProfileViewModel.fetchUserData(email)

        // Collect the user data from ViewModel
        lifecycleScope.launchWhenStarted {
            editProfileViewModel.userData.collect { user ->

                Log.d("Data ----->", "${user?.email}")
                if (user != null) {
                    currentUser = user
                    binding.edtEmail.setText(user.email)
                    binding.edtPassword.setText(user.password)
                }else {

                    Toast.makeText(applicationContext, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle update action
        binding.btnSaveChanges.setOnClickListener {
            val updatedEmail = binding.edtEmail.text.toString()
            val updatedPassword = binding.edtPassword.text.toString()

            val updatedUser = currentUser.copy(email = updatedEmail, password = updatedPassword)
            editProfileViewModel.updateUserData(updatedUser)
        }

        // Toggle password visibility
//        binding.ivTogglePasswordVisibility.setOnClickListener {
//            isPasswordVisible = PasswordUtils.togglePasswordVisibility(
//                binding.edPassword, binding.ivTogglePasswordVisibility, isPasswordVisible
//            )
//        }

        // Collect the update state
        lifecycleScope.launchWhenStarted {
            editProfileViewModel.updateState.collect { state ->
                if (state.isLoading) {
                    // Show loading indicator (ProgressBar)
                } else {
                    // Hide loading indicator
                    if (state.isSuccess) {
                        Toast.makeText(applicationContext, "Profile Updated!", Toast.LENGTH_SHORT)
                            .show()
                        // Optionally, navigate back or show success
                    } else {
                        state.errorMessage?.let {
                            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        ///sadhfhds
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
