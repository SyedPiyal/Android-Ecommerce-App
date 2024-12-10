package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title :String,
    val description: String,
    val image :String

)

