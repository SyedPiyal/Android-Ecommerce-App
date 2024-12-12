package com.example.androidecommerceapp.components


import android.content.Context
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.database.OrderDao

import com.example.androidecommerceapp.database.OrderEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OrderStatusUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val orderDao: OrderDao
) : CoroutineWorker(appContext, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val orderId = inputData.getInt("ORDER_ID", 0)

        // Fetch the order from the database and update its status
//        val orderDao = AppDatabase.getInstance(applicationContext).orderDao()
        val order = orderDao.getOrderById(orderId)
        Log.d("orderId---->","$order")

        if (order != null) {
            // Update the order status after the specified time
            val updatedOrder = when(order.status) {
                "Processing" -> order.copy(status = "Shipped")
                "Shipped" -> order.copy(status = "Delivering")
                "Delivering" -> order.copy(status = "Delivered")
                else -> return Result.failure()
            }

            // Save updated order status
            orderDao.updateOrder(updatedOrder)
            showNotification(updatedOrder)

            return Result.success()
        } else {
            return Result.failure()
        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(order: OrderEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationChannel = NotificationChannel(
                    "order_updates",
                    "Order Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(notificationChannel)

                val notification = NotificationCompat.Builder(applicationContext, "order_updates")
                    .setContentTitle("Order Status: ${order.status}")
                    .setContentText("Your order is now ${order.status}")
                    .setSmallIcon(R.drawable.ic_order)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                notificationManager.notify(order.id, notification)
            } else {
                // If permission is not granted, show a message or handle accordingly
                Toast.makeText(applicationContext, "Permission to show notifications is denied.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If API level is less than 33, notifications are allowed by default
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                "order_updates",
                "Order Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)

            val notification = NotificationCompat.Builder(applicationContext, "order_updates")
                .setContentTitle("Order Status: ${order.status}")
                .setContentText("Your order is now ${order.status}")
                .setSmallIcon(R.drawable.ic_order)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            notificationManager.notify(order.id, notification)
        }
    }

}
