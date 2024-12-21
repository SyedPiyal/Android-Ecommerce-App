package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoriteEntity(
    @PrimaryKey val productId: Int
)

