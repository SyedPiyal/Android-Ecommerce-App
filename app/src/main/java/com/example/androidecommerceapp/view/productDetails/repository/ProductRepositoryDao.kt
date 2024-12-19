package com.example.androidecommerceapp.view.productDetails.repository

import com.example.androidecommerceapp.database.ProductDao
import com.example.androidecommerceapp.database.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryDao @Inject constructor(
    private val productDao: ProductDao
) {

    suspend fun addProductToFavorites(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun removeProductFromFavorites(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    fun getFavorites(): Flow<List<ProductEntity>> {
        return productDao.getAllFavorites()
    }
}
