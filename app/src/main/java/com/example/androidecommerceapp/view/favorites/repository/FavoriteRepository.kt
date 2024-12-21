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
}
