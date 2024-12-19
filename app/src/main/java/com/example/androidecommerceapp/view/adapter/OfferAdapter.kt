package com.example.androidecommerceapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.view.dataModel.Offer

class OfferAdapter(private val offers: List<Offer>) : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.offer_item, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers[position]
        holder.offerImage.setImageResource(offer.imageResId)
        holder.offerText.text = offer.description
    }

    override fun getItemCount(): Int = offers.size

    class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val offerImage: ImageView = view.findViewById(R.id.offer_image)
        val offerText: TextView = view.findViewById(R.id.offer_text)
    }
}
