package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorites")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title :String,
    val description: String,
    val image :String,
    val price : Double

):Serializable

