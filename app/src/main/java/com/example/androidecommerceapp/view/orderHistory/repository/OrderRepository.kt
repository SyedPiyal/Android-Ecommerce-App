package com.example.androidecommerceapp.view.orderHistory.repository

import com.example.androidecommerceapp.database.OrderDao
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.view.dataModel.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderDao: OrderDao) {

    // add Order in Dao
    suspend fun addOrder(order: Product) {
        // Create the OrderEntity object with the fetched product data
        val orderEntity = OrderEntity(
            title = order.title,
            description = order.description,
            image = order.image,
            price = order.price,
            quantity = 1,  // Assume quantity is 1 for now, you can modify as needed
            orderDate = System.currentTimeMillis(),
            status = "Processing"
        )

        // Insert the OrderEntity into the Room database
        orderDao.insertOrder(orderEntity)
    }

    // get all Order from Dao
    fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }
}
