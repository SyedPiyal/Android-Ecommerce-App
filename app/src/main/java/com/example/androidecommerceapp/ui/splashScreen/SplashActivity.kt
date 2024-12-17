package com.example.androidecommerceapp.ui.splashScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.content.SharedPreferences
import androidx.activity.enableEdgeToEdge
import com.example.androidecommerceapp.MainActivity
import com.example.androidecommerceapp.databinding.ActivityLoginBinding
import com.example.androidecommerceapp.databinding.ActivitySplashBinding
import com.example.androidecommerceapp.ui.auth.LoginActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val splashScreenDuration: Long = 2000 // 2 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Wait for splash screen to display, then check login status
        Handler().postDelayed({
            // Check login status in SharedPreferences
            val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false) // Default to false if not found

            // Navigate based on login status
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            // Close SplashActivity so user cannot come back to it by pressing back
            finish()

        }, splashScreenDuration)
    }
}
