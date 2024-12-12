package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.database.OrderDao
import com.example.androidecommerceapp.database.OrderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderDao: OrderDao) {

    suspend fun addOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }
}
