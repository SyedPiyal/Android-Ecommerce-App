package com.example.androidecommerceapp.service

import com.example.androidecommerceapp.dataModel.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {

    // Get all products
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    // Get a product by ID
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Product

    // Get products by category
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>

    // Get categories
    @GET("products/categories")
    suspend fun getCategories(): List<String>
}