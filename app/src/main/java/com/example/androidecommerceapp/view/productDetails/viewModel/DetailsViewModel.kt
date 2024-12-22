package com.example.androidecommerceapp.view.productDetails.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.favorites.repository.FavoriteRepository
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import com.example.androidecommerceapp.view.productDetails.repository.ProductRepositoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _favoriteProducts = MutableLiveData<List<Product>>()
    val favoriteProducts: LiveData<List<Product>> = _favoriteProducts

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    // LiveData to expose product details to the UI
    private val _productDetails = MutableLiveData<ResultState<Product>>()
    val productDetails: LiveData<ResultState<Product>> = _productDetails

    // Fetch all favorite product IDs and then fetch product details for each ID
    fun loadFavorites() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteIds().collect { favoriteEntities ->
                val products = mutableListOf<Product>()
                for (favorite in favoriteEntities) {
                    // Fetch product details for each favorite ID
                    productRepository.getProductById(favorite.productId).collect { result ->
                        when (result) {
                            is ResultState.Success -> products.add(result.data)
                            is ResultState.Error -> Log.e("Favorites", "Error fetching product")
                            else -> {}
                        }
                    }
                }
                _favoriteProducts.value = products
            }
        }
    }

    // Remove a product from favorites
    fun removeProductFromFavorites(productId: Int) {
        viewModelScope.launch {
            favoriteRepository.removeProductFromFavorites(productId)
            loadFavorites() // Reload the favorites after removal
        }
    }



    // Function to add a product to the favorites
    fun addProductToFavorites(productId: Int) {
        viewModelScope.launch {
            favoriteRepository.addProductToFavorites(productId)
        }
    }


    // Function to fetch product details by id
    fun getProductById(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                // Emit the result (success, loading, or error)
//                _productDetails.value = result
                when (result) {
                    is ResultState.Success -> {
                        _productDetails.value = result
                        // Check if the product is favorited
                        checkIfProductIsFavorite(productId)
                    }
                    else -> {
                        _productDetails.value = result
                    }
                }
            }
        }
    }
    // Check if a product is already in favorites
    private fun checkIfProductIsFavorite(productId: Int) {
        viewModelScope.launch {
            val isProductFavorite = favoriteRepository.isProductFavorite(productId) // Implement this function in your repository
            _isFavorite.value = isProductFavorite
        }
    }
}
