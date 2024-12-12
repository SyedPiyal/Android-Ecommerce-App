package com.example.androidecommerceapp.ui.orderHistory

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.databinding.ActivityOrderHistoryBinding
import com.example.androidecommerceapp.ui.adapter.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityOrderHistoryBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private val orderItems = mutableListOf<OrderEntity>() // List to store orders
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize RecyclerView and Adapter
        recyclerView = binding.recyclerViewOrders
        orderAdapter = OrderAdapter(orderItems)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = orderAdapter

        // Fetch orders from ViewModel
        orderHistoryViewModel.getOrders()

        // Observe orders list from ViewModel
        orderHistoryViewModel.orders.observe(this) { orders ->
            if (orders.isEmpty()) {
                binding.emptyStateMessage.visibility = View.VISIBLE
            } else {
                binding.emptyStateMessage.visibility = View.GONE
                orderItems.clear()
                orderItems.addAll(orders)
                orderAdapter.notifyDataSetChanged()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}