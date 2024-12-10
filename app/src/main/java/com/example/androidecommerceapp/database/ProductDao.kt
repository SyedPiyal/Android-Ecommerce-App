package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM favorites WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): ProductEntity?
}
