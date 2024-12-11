package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    // Get all cart items
    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flow<List<CartEntity>>

    // Insert or update a cart item (on conflict, it will replace the existing one)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartEntity)

    // Update a cart item (for example, when quantity changes)
    @Update
    suspend fun updateCartItem(cartItem: CartEntity)

    // Delete a cart item
    @Delete
    suspend fun deleteCartItem(cartItem: CartEntity)
}

