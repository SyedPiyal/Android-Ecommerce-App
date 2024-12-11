package com.example.androidecommerceapp.repository

import com.example.androidecommerceapp.database.CartDao
import com.example.androidecommerceapp.database.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    suspend fun addItemToCart(cartItem: CartEntity) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun removeItemFromCart(cartItem: CartEntity) {
        cartDao.deleteCartItem(cartItem)
    }

    fun getCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }
}
