package com.example.androidecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        // Set product image, name, and price
        holder.productName.text = item.productName
        holder.productPrice.text = "$${item.productPrice}"

        // Load image (using a local drawable resource)
        holder.productImage.setImageResource(item.productImageResId)

        // Handle remove button click
        holder.removeButton.setOnClickListener {
            // Here, you could remove the item from the list and notify the adapter
            favoriteItems.removeAt(position)
            notifyItemRemoved(position)

            // Update the empty state visibility
            if (favoriteItems.isEmpty()) {
                // Update UI visibility in fragment
                (holder.itemView.context as FavoritesFragment).updateEmptyState()
            }
        }
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return favoriteItems.size
    }
}
