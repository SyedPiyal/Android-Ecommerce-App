package com.example.androidecommerceapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.repository.ProductRepository
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    // LiveData for products
    private val _products = MutableLiveData<ResultState<List<Product>>>()
    val products: LiveData<ResultState<List<Product>>> get() = _products

    // Use Flow to collect data asynchronously and update LiveData
    fun fetchProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect { resource ->
                _products.value = resource
            }
        }
    }
}
