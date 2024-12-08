package com.example.androidecommerceapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Category
import com.example.androidecommerceapp.dataModel.Offer
import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.databinding.FragmentHomeBinding
import com.example.androidecommerceapp.ui.adapter.CategoryAdapter
import com.example.androidecommerceapp.ui.adapter.OfferAdapter
import com.example.androidecommerceapp.ui.adapter.ProductAdapter
import com.example.androidecommerceapp.ui.productDetails.ProductDetailsActivity
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.utils.ToastTypeM
import com.example.androidecommerceapp.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter


    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sample data
        val offers = listOf(
            Offer(R.drawable.shoe, "50% OFF \non Shoes"),
            Offer(R.drawable.shoe, "Buy 1 Get 1 Free \non Clothing"),
            Offer(R.drawable.shoe2, "Up to 70% OFF \non Electronics")
        )

        // Sample categories
        val categories = listOf(
            Category(R.drawable.shoe2, "Pant"),
            Category(R.drawable.shoe2, "Shirt"),
            Category(R.drawable.shoe2, "Shoes")
        )

        recyclerView = binding.offerSlider
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        offerAdapter = OfferAdapter(offers)
        recyclerView.adapter = offerAdapter

        // catagory rv
        // Set up the RecyclerView
        recyclerView = binding.categorySlider
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(categories)
        recyclerView.adapter = categoryAdapter



        // Set up the RecyclerView with StaggeredGridLayoutManager
        val productGridView: RecyclerView = binding.productRvView
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Observe LiveData from the ViewModel
        homeViewModel.products.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is ResultState.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE // Show loading
                    binding.productRvView.visibility = View.GONE // Hide the RecyclerView while loading
                }
                is ResultState.Success -> {
//                    binding.progressBar.visibility = View.GONE // Hide loading
                    binding.productRvView.visibility = View.VISIBLE // Show the RecyclerView
                    // Update the adapter with the fetched products
                    productAdapter = ProductAdapter(resource.data)
                    recyclerView.adapter = productAdapter
                }
                is ResultState.Error -> {
//                    binding.progressBar.visibility = View.GONE // Hide loading
                    ToastUtils.showCustomToast(
                        requireContext(),
                         "Loading Failed",
                        ToastTypeM.ERROR
                    )// Show error
                }
            }
        })

        // Fetch products when the fragment is created
        homeViewModel.fetchProducts()

        productGridView.layoutManager = layoutManager
        productGridView.adapter = productAdapter

        binding.tvSeeAll.setOnClickListener {
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}