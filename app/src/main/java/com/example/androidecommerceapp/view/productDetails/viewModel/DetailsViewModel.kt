package com.example.androidecommerceapp.view.productDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.view.productDetails.repository.ProductRepositoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val productRepositoryDao: ProductRepositoryDao,
) : ViewModel() {

    private val _favorites = MutableLiveData<List<ProductEntity>>()
    val favorites: LiveData<List<ProductEntity>> get() = _favorites

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun addToFavorites(product: ProductEntity) {
        viewModelScope.launch {
            productRepositoryDao.addProductToFavorites(product)
            _isFavorite.postValue(true)
        }
    }

    fun checkIfFavorite(productId: Int) {
        viewModelScope.launch {
            val favoritesList = _favorites.value ?: emptyList()
            _isFavorite.postValue(favoritesList.any { it.id == productId })
        }
    }


    // Initialize the favorites list to check product favorite status
    fun initializeFavorites() {
        viewModelScope.launch {
            productRepositoryDao.getFavorites().collect {
                _favorites.postValue(it)
            }
        }
    }

    fun removeFromFavorites(product: ProductEntity) {
        viewModelScope.launch {
            productRepositoryDao.removeProductFromFavorites(product)
            _isFavorite.postValue(false)

        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            productRepositoryDao.getFavorites().collect {
                _favorites.postValue(it)
            }
        }
    }
}
