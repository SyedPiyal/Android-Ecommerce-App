package com.example.androidecommerceapp.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidecommerceapp.ui.MainActivity
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivityLoginBinding
import com.example.androidecommerceapp.utils.PasswordUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
//    private val editProfileViewModel: EditProfileViewModel by viewModels()

    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorSelected)

        binding.buttonLoginLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString()
            val password = binding.edPasswordLogin.text.toString()
            authViewModel.loginUser(email, password)
            // Collecting the loginState from the ViewModel
            lifecycleScope.launch {
                authViewModel.loginState.collect { state ->
                    if (state.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE

                        // Handle success or error
                        state.errorMessage?.let {
                            Toast.makeText(applicationContext, "Invalid User", Toast.LENGTH_SHORT)
                                .show()
                        }

                        if (state.isSuccess) {
                            Toast.makeText(
                                applicationContext,
                                "Login Successful!",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            // Save the login state to SharedPreferences
                            val sharedPreferences: SharedPreferences =
                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.putString("email", email)
                            editor.apply()

                            // pass user data to edit profile
//                            val user = User(id = 1, email = email, password = password)
//                            Log.d("LoginActivity---->", "Setting user: $user")
//                            editProfileViewModel.setUser(user)


                            // Navigate to the home screen or main activity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                            intent.putExtra("user_data", user)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

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





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}