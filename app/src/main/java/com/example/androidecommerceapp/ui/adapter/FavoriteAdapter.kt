package com.example.androidecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.FavoriteItem
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.ui.favorites.FavoritesFragment


class FavoriteAdapter(
    private val favoriteItems: MutableList<ProductEntity>,
    private val onItemClicked: (ProductEntity) -> Unit,
    private val onRemoveClicked: (ProductEntity) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.favorite_image)
        val productName: TextView = itemView.findViewById(R.id.favorite_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.favorite_product_price)
        val removeButton: ImageView = itemView.findViewById(R.id.favorite_remove_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val product = favoriteItems[position]

        // Set product name, price, and image
        holder.productName.text = product.title
        holder.productPrice.text = "$${product.price}" // Adjust according to actual price field
        Glide.with(holder.itemView.context)
            .load(product.image)  // Assuming product.image contains the image URL
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.productImage)

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClicked(product) // Pass the clicked product to the fragment
        }

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            onRemoveClicked(product)
        }
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }
}
