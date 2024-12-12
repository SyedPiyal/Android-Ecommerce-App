package com.example.androidecommerceapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val image: String,
    val price: Double,
    val quantity: Int,
    val orderDate: Long,
    var status: String = "Processing"
) : Serializable
