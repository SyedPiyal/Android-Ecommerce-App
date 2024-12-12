package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Int): OrderEntity?

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}
