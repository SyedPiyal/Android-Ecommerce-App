package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val price: Double,
    val quantity: Int = 1 // Quantity of the product in the cart
): Serializable
