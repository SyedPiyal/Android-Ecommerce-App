package com.example.androidecommerceapp.view.myCart.repository

import com.example.androidecommerceapp.database.CartDao
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.view.dataModel.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    // Function to get all cart items
    suspend fun getCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()  // Returns a flow of all cart items
    }

    // Function to add an item to the cart
    suspend fun addItemToCart(cartItem: Product) {
        val cartItem = CartEntity(
            id = cartItem.id,
            title = cartItem.title,
            description = cartItem.description,
            image = cartItem.image,
            price = cartItem.price,
            quantity = 1
        )
        cartDao.insertCartItem(cartItem)  // Insert or update cart item based on the primary key
    }

    // Function to update an item in the cart (usually called when quantity changes)
    suspend fun updateCartItem(cartItem: CartEntity) {
        cartDao.updateCartItem(cartItem)  // Update the item with new quantity
    }

    // Function to remove an item from the cart
    suspend fun removeFromCart(cartItem: CartEntity) {
        cartDao.deleteCartItem(cartItem)  // Remove the cart item from the database
    }

    /// others ---> convert

}
