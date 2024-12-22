package com.example.androidecommerceapp.view.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.databinding.ItemFavoriteBinding
import com.example.androidecommerceapp.view.dataModel.Product


class FavoriteAdapter(private val onRemoveClickListener: (Int) -> Unit, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var favoriteProducts = mutableListOf<Product>()

    // Create ViewHolder for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = favoriteProducts[position]
        Log.d("FavoriteAdapter---->", "Binding product: ${product.title}, Price: ${product.price}")
        holder.bind(product, onRemoveClickListener, onItemClick)
    }

    // Return the total count of items
    override fun getItemCount(): Int {
        return favoriteProducts.size
    }

    // Update the list of favorite products
    fun submitList(products: List<Product>) {
        favoriteProducts.clear()
        favoriteProducts.addAll(products)
        notifyDataSetChanged()
    }

    // ViewHolder class that binds individual product data to the views
    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, onRemoveClickListener: (Int) -> Unit, onItemClick: (Int) -> Unit) {
            binding.favoriteProductName.text = product.title
            binding.favoriteProductPrice.text = "$${product.price}"
            Glide.with(binding.favoriteImage.context).load(product.image)
                .into(binding.favoriteImage)


            // Set click listener for the item
            itemView.setOnClickListener {
                onItemClick(product.id) // Pass the product id when item is clicked
            }
            // Handle remove button click
            binding.favoriteRemoveButton.setOnClickListener {
                onRemoveClickListener(product.id)
            }
        }
    }
}

