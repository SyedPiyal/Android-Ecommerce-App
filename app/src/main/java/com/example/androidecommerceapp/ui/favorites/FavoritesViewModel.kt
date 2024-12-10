package com.example.androidecommerceapp.ui.favorites

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
class FavoritesViewModel  @Inject constructor(
    private val productRepositoryDao: ProductRepositoryDao
) : ViewModel() {

    private val _favorites = MutableLiveData<List<ProductEntity>>()
    val favorites: LiveData<List<ProductEntity>> get() = _favorites

    // Get the list of favorites from the database
    fun getFavorites() {
        viewModelScope.launch {
            productRepositoryDao.getFavorites().collect {
                _favorites.postValue(it)
            }
        }
    }
}