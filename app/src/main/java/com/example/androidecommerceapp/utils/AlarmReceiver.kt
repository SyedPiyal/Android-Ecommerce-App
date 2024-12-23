package com.example.androidecommerceapp.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {


    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
            val serviceIntent = Intent(context, SyncService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        } else {
            // Handle no WiFi connectivity
            Toast.makeText(context, "No WiFi connection, sync failed", Toast.LENGTH_SHORT).show()
        }
    }
}
