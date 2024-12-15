package com.example.androidecommerceapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidecommerceapp.MainActivity
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivitySignupBinding
import com.example.androidecommerceapp.utils.PasswordUtils
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.utils.ToastTypeM
import com.example.androidecommerceapp.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val authViewModel: AuthViewModel by viewModels()
    private var isPasswordVisible: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonRegisterRegister.setOnClickListener {
            val email = binding.edEmailRegister.text.toString()
            val password = binding.edPasswordRegister.text.toString()

            authViewModel.checkIfUserExists(email)
            authViewModel.registerUser(email, password)
        }
        // Toggle password visibility
        binding.ivTogglePasswordVisibility.setOnClickListener {
            isPasswordVisible = PasswordUtils.togglePasswordVisibility(
                binding.edPasswordRegister, binding.ivTogglePasswordVisibility, isPasswordVisible
            )
        }

        // Collecting the signupState from the ViewModel
        lifecycleScope.launch {
            authViewModel.signupState.collect { state ->
                if (state.isSuccess) {
                    // Show success message
                    Toast.makeText(
                        applicationContext,
                        "Registration Successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to LoginActivity
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                if (state.isLoading) {
                    // Show loading indicator (for example, a ProgressBar)
                } else {
                    // Hide loading indicator
                    state.errorMessage?.let {
                        Toast.makeText(
                            applicationContext,
                            "User Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}