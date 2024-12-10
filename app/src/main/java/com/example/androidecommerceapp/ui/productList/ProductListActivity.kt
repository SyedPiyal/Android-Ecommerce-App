package com.example.androidecommerceapp.ui.productList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.ActivityProductListBinding
import com.example.androidecommerceapp.ui.adapter.ProductAdapter
import com.example.androidecommerceapp.ui.productDetails.DetailsActivity
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var productAdapter: ProductAdapter
    private val productListViewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedCategory = intent.getStringExtra("CATEGORY")

        selectedCategory.let {
            Log.d("tag", "Data is here ---> ${it}")
        }

        // Initialize RecyclerView and Adapter
        productAdapter = ProductAdapter(mutableListOf()) { product ->
            // Handle item click (e.g., navigate to product details)
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("PRODUCT", product)
            }
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = productAdapter

        // Observe the products from the ViewModel
        productListViewModel.products.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    productAdapter.setData(result.data)
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // Fetch products by category if category is available
        selectedCategory?.let {
            productListViewModel.getProductsByCategory(it)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}