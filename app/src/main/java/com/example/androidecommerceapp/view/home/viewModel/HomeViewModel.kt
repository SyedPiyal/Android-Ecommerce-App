package com.example.androidecommerceapp.view.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<ResultState<List<Product>>>()
    val products: LiveData<ResultState<List<Product>>> get() = _products

    private val _categories = MutableLiveData<ResultState<List<String>>>()
    val categories: LiveData<ResultState<List<String>>> get() = _categories

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                _products.postValue(it)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            productRepository.getCategories().collect {
                _categories.postValue(it)
            }
        }
    }
}
