package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val price: Double,
    var quantity: Int =1
) : Serializable {

    // Calculate the total price for the item based on quantity
    fun getTotalPrice(): Double {
        return price * quantity
    }
}
