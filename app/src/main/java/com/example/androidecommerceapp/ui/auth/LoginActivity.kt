package com.example.androidecommerceapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidecommerceapp.MainActivity
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivityLoginBinding
import com.example.androidecommerceapp.ui.productDetails.ProductDetailsActivity
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString()
            val password = binding.edPasswordLogin.text.toString()
            authViewModel.login(email, password)
        }

        // Observe login and signup state
        lifecycleScope.launchWhenStarted {
            authViewModel.authState.collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        // Show loading spinner
                    }
                    is ResultState.Success -> {
                        // Handle success (e.g., navigate to the main screen)
                        Toast.makeText(this@LoginActivity, "Success: ${result.data}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Error -> {
                        // Show error message
                        Toast.makeText(this@LoginActivity, result.exception.message, Toast.LENGTH_SHORT).show()
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