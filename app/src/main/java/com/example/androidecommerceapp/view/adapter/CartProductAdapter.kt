package com.example.androidecommerceapp.view.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.databinding.ItemCartBinding


class CartProductAdapter(
    private val cartItems: MutableList<CartEntity>,
    private val onRemoveItemClick: (CartEntity) -> Unit,
    private val onQuantityChanged: (CartEntity) -> Unit
) : RecyclerView.Adapter<CartProductAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartEntity) {
            binding.tvTitle.text = cartItem.title
            binding.tvPrice.text = "$${cartItem.price * cartItem.quantity}"
            binding.tvItemQuantity.text = "${cartItem.quantity * cartItem.quantity}"
            binding.tvQuantity.text = "Quantiry :${cartItem.quantity}"

            Glide.with(binding.root.context).load(cartItem.image).into(binding.ivImage)

            binding.btnDecrease.setOnClickListener {
                if (cartItem.quantity > 1) {
                    cartItem.quantity -= 1
                    Log.d("CartAdapter--->", "Decreased quantity: ${cartItem.quantity}")
                    Log.d("CartAdapter--->", "Decreased price: ${cartItem.price * cartItem.quantity}")
                    binding.tvItemQuantity.text = cartItem.quantity.toString()
                    binding.tvQuantity.text = cartItem.quantity.toString()
                    onQuantityChanged(cartItem)  // Update the quantity in the repository
                }
            }

            binding.btnIncrease.setOnClickListener {
                cartItem.quantity += 1
                Log.d("CartAdapter--->", "Increase price: ${cartItem.price * cartItem.quantity}")
                Log.d("CartAdapter--->", "Increase quantity: ${cartItem.quantity}")
                binding.tvItemQuantity.text = cartItem.quantity.toString()
                binding.tvQuantity.text = cartItem.quantity.toString()
                onQuantityChanged(cartItem)  // Update the quantity in the repository
            }

            binding.btnRemove.setOnClickListener {
                onRemoveItemClick(cartItem)
            }
        }
    }
}
