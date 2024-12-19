package com.example.androidecommerceapp.view.auth

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivitySignupBinding
import com.example.androidecommerceapp.view.auth.viewModel.AuthViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorSelected)


        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            authViewModel.checkIfUserExists(email)
            authViewModel.registerUser(email, password)
        }
        // Toggle password visibility


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
                    // Show loading indicator
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

        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}