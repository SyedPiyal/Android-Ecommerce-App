package com.example.androidecommerceapp.utils



import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidecommerceapp.components.OrderStatusUpdateWorker
import java.util.concurrent.TimeUnit

// This class is responsible for scheduling order status updates using WorkManager
object OrderStatusScheduler {

    // Function to schedule WorkManager tasks to update order status
    fun scheduleOrderStatusUpdates(orderId: Int) {
        // Schedule WorkManager for the first status update (Shipped) after 30 seconds
        val workRequestShipped = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .addTag("01")
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Schedule WorkManager for the second status update (Delivering) after 1 minute
        val workRequestDelivering = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Schedule WorkManager for the final status update (Delivered) after 90 seconds
        val workRequestDelivered = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .setInitialDelay(90, TimeUnit.SECONDS)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Enqueue WorkManager tasks
        WorkManager.getInstance().enqueue(workRequestShipped)
        WorkManager.getInstance().enqueue(workRequestDelivering)
        WorkManager.getInstance().enqueue(workRequestDelivered)
    }
}
