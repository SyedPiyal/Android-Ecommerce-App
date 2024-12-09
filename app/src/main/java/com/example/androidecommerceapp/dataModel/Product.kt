package com.example.androidecommerceapp.dataModel

import java.io.Serializable


data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val image: String,
    val category: String
): Serializable
