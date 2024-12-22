package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun delete(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorites_table")
    fun getAllFavoriteIds(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites_table WHERE productId = :productId")
    suspend fun getFavoriteByProductId(productId: Int): FavoriteEntity?

    @Query("DELETE FROM favorites_table WHERE productId = :productId")
    suspend fun removeFavoriteByProductId(productId: Int)
}

