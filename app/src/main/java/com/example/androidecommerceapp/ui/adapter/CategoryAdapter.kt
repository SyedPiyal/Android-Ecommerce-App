package com.example.androidecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Category

class CategoryAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryImage.setImageResource(category.imageResId)  // Set the image resource
        holder.categoryTitle.text = category.name  // Set the category name
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.category_image)
        val categoryTitle: TextView = view.findViewById(R.id.category_title)
    }
}
