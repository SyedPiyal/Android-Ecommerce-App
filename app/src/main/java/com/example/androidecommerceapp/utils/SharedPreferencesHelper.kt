package com.example.androidecommerceapp.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferencesHelper @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "KEY_TOKEN"
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
