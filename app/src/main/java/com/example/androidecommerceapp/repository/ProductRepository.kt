package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.service.FakeStoreApiService
import com.example.androidecommerceapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class ProductRepository @Inject constructor(
    private val apiService: FakeStoreApiService
) {

    // Use Flow to emit data asynchronously
    suspend fun getProducts(): Flow<ResultState<List<Product>>> = flow {
        emit(ResultState.Loading) // emit loading state
        try {
            // Fetch data from the API
            val response = apiService.getAllProducts()
            emit(ResultState.Success(response)) // emit success with data
        } catch (e: Exception) {
            emit(ResultState.Error(e)) // emit error
        }
    }
    // This method returns a Flow that emits the result of the API call (Loading, Success, or Error)
    suspend fun getProductById(productId: Int): Flow<ResultState<Product>> = flow {
        emit(ResultState.Loading) // Emit loading state before fetching data
        try {
            // Fetch product by ID from the API
            val response = apiService.getProductById(productId)
            emit(ResultState.Success(response)) // Emit success with the product data
        } catch (e: Exception) {
            emit(ResultState.Error(e)) // Emit error state if something goes wrong
        }
    }

    // for product categories
    suspend fun getCategories(): Flow<ResultState<List<String>>> = flow {
        emit(ResultState.Loading) // Emit loading state
        try {
            // Fetch categories from the API
            val response = apiService.getCategories()
            emit(ResultState.Success(response)) // Emit success with data
        } catch (e: Exception) {
            emit(ResultState.Error(e)) // Emit error state
        }
    }
}
