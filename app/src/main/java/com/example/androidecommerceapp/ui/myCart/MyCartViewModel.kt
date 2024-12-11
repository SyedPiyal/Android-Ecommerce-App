package com.example.androidecommerceapp.ui.myCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartEntity>>()
    val cartItems: LiveData<List<CartEntity>> get() = _cartItems

    fun addToCart(cartItem: CartEntity) {
        viewModelScope.launch {
            cartRepository.addItemToCart(cartItem)
        }
    }

    fun removeFromCart(cartItem: CartEntity) {
        viewModelScope.launch {
            cartRepository.removeItemFromCart(cartItem)
        }
    }

    fun getCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect {
                _cartItems.postValue(it)
            }
        }
    }
}
