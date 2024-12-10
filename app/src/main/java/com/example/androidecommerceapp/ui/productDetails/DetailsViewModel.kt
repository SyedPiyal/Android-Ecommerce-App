package com.example.androidecommerceapp.ui.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.repository.ProductRepositoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val productRepositoryDao: ProductRepositoryDao
) : ViewModel() {

    private val _favorites = MutableLiveData<List<ProductEntity>>()
    val favorites: LiveData<List<ProductEntity>> get() = _favorites

    fun addToFavorites(product: ProductEntity) {
        viewModelScope.launch {
            productRepositoryDao.addProductToFavorites(product)
        }
    }

    fun removeFromFavorites(product: ProductEntity) {
        viewModelScope.launch {
            productRepositoryDao.removeProductFromFavorites(product)
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
