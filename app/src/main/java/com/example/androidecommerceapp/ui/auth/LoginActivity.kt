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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidecommerceapp.MainActivity
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivityLoginBinding
import com.example.androidecommerceapp.utils.PasswordUtils
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.utils.ToastTypeM
import com.example.androidecommerceapp.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    private var isPasswordVisible: Boolean = false

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

        binding.tvDontHaveAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Toggle password visibility
        binding.ivTogglePasswordVisibility.setOnClickListener {
            isPasswordVisible = PasswordUtils.togglePasswordVisibility(
                binding.edPasswordLogin, binding.ivTogglePasswordVisibility, isPasswordVisible
            )
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                authViewModel.authState.collect { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            // Show loading spinner
                        }

                        is ResultState.Success -> {
                            // Show success toast
                            ToastUtils.showCustomToast(
                                this@LoginActivity, "Login Successful!",
                                ToastTypeM.SUCCESS
                            )

                            val intent = Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                            startActivity(intent)
                            finish()

                        }

                        is ResultState.Error -> {

                            ToastUtils.showCustomToast(
                                this@LoginActivity,
                                result.exception.message ?: "Login Failed",
                                ToastTypeM.ERROR
                            )
                        }
                    }
                }
            }
        }

        // Observe toast events and display custom toasts
        lifecycleScope.launch {
            authViewModel.toastEvent.collect { toastEvent ->
                ToastUtils.showCustomToast(
                    this@LoginActivity, toastEvent.message,
                    toastEvent.type
                )
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}