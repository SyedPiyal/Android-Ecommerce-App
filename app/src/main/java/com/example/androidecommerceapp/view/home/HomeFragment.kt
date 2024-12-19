package com.example.androidecommerceapp.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.databinding.FragmentHomeBinding
import com.example.androidecommerceapp.view.adapter.CategoryAdapter
import com.example.androidecommerceapp.view.adapter.ProductAdapter
import com.example.androidecommerceapp.view.productDetails.DetailsActivity
import com.example.androidecommerceapp.view.productList.ProductListActivity
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.adapter.OfferAdapter
import com.example.androidecommerceapp.view.dataModel.Offer
import com.example.androidecommerceapp.view.home.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    // adapters
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        // Sample data
        val offers = listOf(
            Offer(R.drawable.shoe, "50% OFF \non Shoes"),
            Offer(R.drawable.shirt, "Buy 2 Get 1 Free \non Clothing"),
            Offer(R.drawable.jacket, "Up to 70% OFF \non Jacket"),
            Offer(R.drawable.watch, "Up to 20% OFF \non Watch")
        )
        recyclerView = binding.rvOfferSlider
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        offerAdapter = OfferAdapter(offers)
        recyclerView.adapter = offerAdapter


        // Initialize CategoryAdapter with an item click listener
        categoryAdapter = CategoryAdapter(emptyList()) { category ->
            // Navigate to ProductListActivity with the selected category
            val intent = Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra("CATEGORY", category) // Pass the selected category
            }
            startActivity(intent)
        }
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
