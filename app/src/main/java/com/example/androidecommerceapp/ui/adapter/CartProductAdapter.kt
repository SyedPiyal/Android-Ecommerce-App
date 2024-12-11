package com.example.androidecommerceapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.databinding.ItemCartBinding


class CartProductAdapter(
    private val cartItems: List<CartEntity>,
    private val onRemoveItemClick: (CartEntity) -> Unit
) : RecyclerView.Adapter<CartProductAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartEntity) {
            binding.tvTitle.text = cartItem.title
            binding.tvPrice.text = "$${cartItem.price}"
            binding.tvQuantity.text = "Quantity: ${cartItem.quantity}"
            Glide.with(binding.root.context).load(cartItem.image).into(binding.ivImage)

            binding.btnRemove.setOnClickListener {
                onRemoveItemClick(cartItem)
            }
        }
    }
}

