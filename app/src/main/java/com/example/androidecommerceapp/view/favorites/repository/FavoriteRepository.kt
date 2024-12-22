package com.example.androidecommerceapp.view.favorites.repository

import com.example.androidecommerceapp.database.FavoriteDao
import com.example.androidecommerceapp.database.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {

    // Insert product into the favorites table
    suspend fun addProductToFavorites(productId: Int) {
        favoriteDao.insert(FavoriteEntity(productId))
    }
    // Get all favorite product IDs
    fun getFavoriteIds(): Flow<List<FavoriteEntity>> {
        return favoriteDao.getAllFavoriteIds()
    }

    // In FavoriteRepository

    suspend fun isProductFavorite(productId: Int): Boolean {
        // Check if the product is already in the favorites
        val favorite = favoriteDao.getFavoriteByProductId(productId)
        return favorite != null
    }
    // In FavoriteRepository

    suspend fun removeProductFromFavorites(productId: Int) {
        // Remove the product from the favorites
        favoriteDao.removeFavoriteByProductId(productId)
    }




}
