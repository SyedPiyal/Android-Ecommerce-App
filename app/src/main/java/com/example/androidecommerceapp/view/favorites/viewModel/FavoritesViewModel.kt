package com.example.androidecommerceapp.view.favorites.viewModel

import android.util.Log
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

//@HiltViewModel
//class FavoritesViewModel  @Inject constructor(
//    private val favoriteRepository: FavoriteRepository,
//    private val productRepository: ProductRepository
//) : ViewModel() {
//
//    private val _favoriteProducts = MutableLiveData<List<Product>>()
//    val favoriteProducts: LiveData<List<Product>> = _favoriteProducts
//
//    private val _isFavorite = MutableLiveData<Boolean>()
//    val isFavorite: LiveData<Boolean> get() = _isFavorite
//
//    // Fetch all favorite product IDs and then fetch product details for each ID
//    fun loadFavorites() {
//        viewModelScope.launch {
//            favoriteRepository.getFavoriteIds().collect { favoriteEntities ->
//                val products = mutableListOf<Product>()
//                for (favorite in favoriteEntities) {
//                    // Fetch product details for each favorite ID
//                    productRepository.getProductById(favorite.productId).collect { result ->
//                        when (result) {
//                            is ResultState.Success -> products.add(result.data)
//                            is ResultState.Error -> Log.e("Favorites", "Error fetching product")
//                            else -> {}
//                        }
//                    }
//                }
//                _favoriteProducts.value = products
//            }
//        }
//    }
//    // Remove a product from favorites
//    fun removeProductFromFavorites(productId: Int) {
//        viewModelScope.launch {
//            favoriteRepository.removeProductFromFavorites(productId)
//            loadFavorites() // Reload the favorites after removal
//        }
//    }
//
//    fun checkIfFavorite(productId: Int) {
//        viewModelScope.launch {
//            val favoritesList = _favoriteProducts.value ?: emptyList()
//            _isFavorite.postValue(favoritesList.any { it.id == productId })
//        }
//    }
//
//
//    // Function to add a product to the favorites
//    fun addProductToFavorites(productId: Int) {
//        viewModelScope.launch {
//            favoriteRepository.addProductToFavorites(productId)
//        }
//    }
//}