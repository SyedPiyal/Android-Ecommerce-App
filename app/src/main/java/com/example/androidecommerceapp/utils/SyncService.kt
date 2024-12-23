package com.example.androidecommerceapp.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SyncService : Service() {

    @Inject
    lateinit var productRepository: ProductRepository

    private lateinit var notificationManager: NotificationManager
    private val notificationChannelId = "data_sync_channel"

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Data Sync Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showSyncNotification("Syncing data...")

        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val products = homeRepository.getProducts()
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }

            // Collect the flow from repository and show notification according to that
            productRepository.getProducts().collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showSyncNotification("Syncing data...")
                    }

                    is ResultState.Success -> {
                        showSyncNotification("Data synced successfully!")
                    }

                    is ResultState.Error -> {
                        showSyncNotification("Sync failed: ${result.exception.message}")
                    }
                }
            }

            // stop the service after work
            // also stop notification
            stopSelf()
        }

        return START_NOT_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun showSyncNotification(message: String) {
        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Data Sync...")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_sync)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
