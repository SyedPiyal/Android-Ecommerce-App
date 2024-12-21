package com.example.androidecommerceapp.view.productDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.home.repository.ProductRepository
import com.example.androidecommerceapp.view.productDetails.repository.ProductRepositoryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    // LiveData to expose product details to the UI
    private val _productDetails = MutableLiveData<ResultState<Product>>()
    val productDetails: LiveData<ResultState<Product>> = _productDetails

    // Function to fetch product details by id
    fun getProductById(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                // Emit the result (success, loading, or error)
                _productDetails.value = result
            }
        }
    }



}
