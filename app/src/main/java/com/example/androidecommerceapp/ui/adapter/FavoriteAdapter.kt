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
import com.example.androidecommerceapp.ui.favorites.FavoritesFragment


class FavoriteAdapter(private val favoriteItems: MutableList<FavoriteItem>) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    // ViewHolder class to hold the views for each item
    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.favorite_image)
        val productName: TextView = itemView.findViewById(R.id.favorite_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.favorite_product_price)
        val removeButton: ImageView = itemView.findViewById(R.id.favorite_remove_button)
    }

    // Creates a new view holder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    // Binds data to the views in the ViewHolder
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favoriteItems[position]

        // Set product name and price
        holder.productName.text = item.productName
        holder.productPrice.text = "$${item.productPrice}"

        // Use Glide to load the product image URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(item.productImageResId)  // Load the image URL from the API response
            .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
            .error(R.drawable.error_image)  // Error image if loading fails
            .into(holder.productImage)

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            favoriteItems.removeAt(position)
            notifyItemRemoved(position)
            // Update empty state visibility
            if (favoriteItems.isEmpty()) {
                (holder.itemView.context as FavoritesFragment).updateEmptyState()
            }
        }
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return favoriteItems.size
    }
}
