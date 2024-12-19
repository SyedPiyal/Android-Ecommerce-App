package com.example.androidecommerceapp.view.productList.viewModel

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
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<ResultState<List<Product>>>()
    val products: LiveData<ResultState<List<Product>>> = _products

    // Function to fetch products by category
    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            productRepository.getProductsByCategory(category).collect { result ->
                _products.postValue(result)
            }
        }
    }
}
