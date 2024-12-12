package com.example.androidecommerceapp.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Order
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.databinding.ItemOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class OrderAdapter(private val orders: List<OrderEntity>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productTitle: TextView = itemView.findViewById(R.id.orderName)
        val productStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val productPrice: TextView = itemView.findViewById(R.id.orderTotal)
        val orderDate: TextView = itemView.findViewById(R.id.orderDate)
        val orderQuantity: TextView = itemView.findViewById(R.id.orderQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.productTitle.text = order.title
        holder.productStatus.text = "Processing"
        holder.productPrice.text = "Total Price $${order.price}"
        holder.orderQuantity.text = "Quantity : 1"
        holder.orderDate.text =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(order.orderDate))
    }

    override fun getItemCount(): Int = orders.size
}
