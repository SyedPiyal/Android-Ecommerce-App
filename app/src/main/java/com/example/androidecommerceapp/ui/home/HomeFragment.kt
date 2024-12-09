package com.example.androidecommerceapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidecommerceapp.databinding.FragmentHomeBinding
import com.example.androidecommerceapp.ui.adapter.CategoryAdapter
import com.example.androidecommerceapp.ui.adapter.ProductAdapter
import com.example.androidecommerceapp.ui.productDetails.DetailsActivity
import com.example.androidecommerceapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    // adapters
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        // for categories
        // Initialize RecyclerView for categories
        categoryAdapter = CategoryAdapter(emptyList()) // Empty list initially
        binding.recyclerViewCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCategories.adapter = categoryAdapter

        // Observe categories from the ViewModel
        homeViewModel.categories.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    categoryAdapter.setData(result.data)
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Trigger the API call to get categories
        homeViewModel.getCategories()

        // Initialize RecyclerView
//        productAdapter = ProductAdapter(mutableListOf()) // Empty list initially

        // Initialize RecyclerView with an onItemClick listener to navigate
//        productAdapter = ProductAdapter(mutableListOf()) {
//            // On item click, navigate to ProductDetailsActivity
//            val intent = Intent(requireContext(), DetailsActivity::class.java)
//            startActivity(intent)
//        }

        productAdapter = ProductAdapter(mutableListOf()) { product ->
            // Handle item click and navigate to ProductDetailsActivity
            val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("PRODUCT", product)
                }
            startActivity(intent)
        }
        binding.rvProduct.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Initialize adapter with item click listener
        binding.rvProduct.adapter = productAdapter

        // Observe the products from the ViewModel
        homeViewModel.products.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Show loading indicator (you can add a progress bar)
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    // Hide loading and update the adapter
                    binding.progressBar.visibility = View.GONE
                    productAdapter.setData(result.data)
                }

                is ResultState.Error -> {
                    // Handle error (e.g., show a toast or snackbar)
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // Trigger API call
        homeViewModel.getProducts()



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
