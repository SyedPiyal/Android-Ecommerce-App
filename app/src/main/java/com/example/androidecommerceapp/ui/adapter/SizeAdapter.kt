package com.example.androidecommerceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.ShoeSize

class SizeAdapter(private val sizes: List<ShoeSize>) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    // ViewHolder class
    inner class SizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sizeTextView: TextView = itemView.findViewById(R.id.sizeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_size, parent, false)
        return SizeViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val shoeSize = sizes[position]
        holder.sizeTextView.text = shoeSize.size
    }

    override fun getItemCount(): Int {
        return sizes.size
    }
}
