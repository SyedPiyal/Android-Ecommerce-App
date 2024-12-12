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

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _totalQuantity = MutableLiveData<Int>()
    val totalQuantity: LiveData<Int> get() = _totalQuantity

    fun addToCart(cartItem: CartEntity) {
        viewModelScope.launch {
            cartRepository.addItemToCart(cartItem)
            getCartItems()
        }
    }

    fun getCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.postValue(items)
                updateTotalPrice(items)  // Recalculate the total price
                updateTotalQuantity(items)
            }
        }
    }

    fun updateCartItem(cartItem: CartEntity) {
        viewModelScope.launch {
            cartRepository.updateCartItem(cartItem)
            getCartItems()  // Refresh the cart items after update
        }
    }

    fun removeFromCart(cartItem: CartEntity) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
            getCartItems()  // Refresh the cart items after removal
        }
    }

    private fun updateTotalPrice(cartItems: List<CartEntity>) {
        val total = cartItems.sumOf { it.getTotalPrice() }
        _totalPrice.postValue(total)
    }

    private fun updateTotalQuantity(cartItems: List<CartEntity>) {
        val total = cartItems.sumOf { it.getTotalQuantity() }
        _totalQuantity.postValue(total)
    }


}
