package com.example.androidecommerceapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorites_table")
    fun getAllFavoriteIds(): Flow<List<FavoriteEntity>>
}

