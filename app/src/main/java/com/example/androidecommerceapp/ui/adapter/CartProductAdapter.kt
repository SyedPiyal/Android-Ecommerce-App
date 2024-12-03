package com.example.androidecommerceapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.dataModel.CartProduct
import com.example.androidecommerceapp.databinding.CartProductItemBinding


class CartProductAdapter(private val productList: List<CartProduct>) :
    RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    inner class CartProductViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.imageCartProduct.setImageResource(cartProduct.image)
            binding.tvProductCartName.text = cartProduct.name
            binding.tvProductCartPrice.text = cartProduct.price
            binding.tvCartProductQuantity.text = cartProduct.quantity.toString()
            binding.imageCartProductColor.setImageResource(cartProduct.color)
            binding.tvCartProductSize.text = cartProduct.size

            // Handle plus and minus image clicks (if necessary)
            binding.imagePlus.setOnClickListener {
                // Increase quantity
            }
            binding.imageMinus.setOnClickListener {
                // Decrease quantity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val binding =
            CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.bind(currentProduct)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
