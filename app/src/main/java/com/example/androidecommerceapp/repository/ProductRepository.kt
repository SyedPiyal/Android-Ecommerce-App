package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.service.FakeStoreApiService
import com.example.androidecommerceapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class ProductRepository @Inject constructor(
    @Named("fakestoreapi") private val apiService: FakeStoreApiService
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
}
