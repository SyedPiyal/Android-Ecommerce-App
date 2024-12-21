package com.example.androidecommerceapp.view.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.FavoriteEntity
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.favorites.repository.FavoriteRepository
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel  @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    // List of favorite product IDs
    private val _favoriteEntityIds = MutableLiveData<List<FavoriteEntity>>()
    val favoriteEntityIds: LiveData<List<FavoriteEntity>> = _favoriteEntityIds

    // Function to add a product to the favorites
    fun addProductToFavorites(productId: Int) {
        // Use viewModelScope for coroutines tied to the ViewModel lifecycle
        viewModelScope.launch {
            favoriteRepository.addProductToFavorites(productId)
        }
    }

    // Fetch favorite product IDs
    fun fetchFavoriteProductIds() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteIds().collect { favoriteList ->
                _favoriteEntityIds.value = favoriteList
            }
        }
    }

    // Fetch product details by ID
    suspend fun getProductById(productId: Int): Flow<ResultState<Product>> {
        return productRepository.getProductById(productId)
    }

}