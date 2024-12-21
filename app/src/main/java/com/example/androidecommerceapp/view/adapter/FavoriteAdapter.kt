package com.example.androidecommerceapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.databinding.ItemFavoriteBinding
import com.example.androidecommerceapp.view.dataModel.Product


class FavoriteAdapter(
    private val context: Context,
    private val products: List<Product>
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.favoriteProductName.text = product.title
            binding.favoriteProductPrice.text = "$${product.price}"
            Glide.with(context).load(product.image).into(binding.favoriteImage)
        }
    }
}
