package com.example.androidecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Category
import com.example.androidecommerceapp.databinding.ItemCategoryBinding

class CategoryAdapter(private var categories: List<String>,
                      private val onCategoryClick: (String) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.textViewCategory.text = category

        // Set OnClickListener for the category item
        holder.itemView.setOnClickListener {
            onCategoryClick(category) // Pass clicked category
        }
    }

    override fun getItemCount(): Int = categories.size

    fun setData(newCategories: List<String>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}

