package com.example.androidecommerceapp.ui.orderHistory

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Order
import com.example.androidecommerceapp.databinding.ActivityOrderHistoryBinding
import com.example.androidecommerceapp.ui.adapter.OrderAdapter

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var orderList: List<Order>

    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize RecyclerView
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)
        orderList = getOrderHistory()  // This should be replaced with your data source

        if (orderList.isEmpty()) {
            binding. emptyStateMessage.visibility = View.VISIBLE
            binding. recyclerViewOrders.visibility = View.GONE
        } else {
            orderAdapter = OrderAdapter(orderList)
            binding.recyclerViewOrders.adapter = orderAdapter
            binding.emptyStateMessage.visibility = View.GONE
            binding.recyclerViewOrders.visibility = View.VISIBLE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun getOrderHistory(): List<Order> {
        // Replace with actual logic to fetch order data (e.g., from a database or API)
        return listOf(
            Order("Order 1", "12/01/2024", "Completed", "$50.00"),
            Order("Order 2", "12/02/2024", "Shipped", "$30.00")
        )
    }

}