package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.database.OrderDao
import com.example.androidecommerceapp.database.OrderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderRepository @Inject constructor(private val orderDao: OrderDao) {

    // add Order in Dao
    suspend fun addOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    // get all Order from Dao
    fun getAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }
}
