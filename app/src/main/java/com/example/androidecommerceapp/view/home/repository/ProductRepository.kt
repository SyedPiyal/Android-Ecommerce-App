package com.example.androidecommerceapp.view.home.repository

import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.service.FakeStoreApiService
import com.example.androidecommerceapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: FakeStoreApiService
) {

    // Use Flow to emit data asynchronously
    suspend fun getProducts(): Flow<ResultState<List<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            // Fetch data from the API
            val response = apiService.getAllProducts()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    // This method returns a Flow that emits the result of the API call (Loading, Success, or Error)
    suspend fun getProductById(productId: Int): Flow<ResultState<Product>> = flow {
        emit(ResultState.Loading)
        try {
            // Fetch product by ID from the API
            val response = apiService.getProductById(productId)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    // for product categories
    suspend fun getCategories(): Flow<ResultState<List<String>>> = flow {
        emit(ResultState.Loading)
        try {
            // Fetch categories from the API
            val response = apiService.getCategories()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    // Fetch products by category
    suspend fun getProductsByCategory(category: String): Flow<ResultState<List<Product>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = apiService.getProductsByCategory(category)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }
}
