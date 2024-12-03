package com.example.androidecommerceapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

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

        // Sample data for the products
        val products = listOf(
            Product(R.drawable.shoe2, "Nike", "$ 1200"),
            Product(R.drawable.shoe2, "Adidas", "$ 1200"),
            Product(R.drawable.shoe2, "Puma", "$ 1200"),
            Product(R.drawable.shoe2, "Reebok", "$ 1200"),
            Product(R.drawable.shoe2, "Nike", "$ 1200"),
            Product(R.drawable.shoe2, "Adidas", "$ 1200"),
            Product(R.drawable.shoe2, "Puma", "$ 1200"),
            Product(R.drawable.shoe2, "Reebok", "$ 1200"),
        )

        // Set up the RecyclerView with StaggeredGridLayoutManager
        val productGridView: RecyclerView = binding.productRvView
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) // 2 columns
        val productAdapter = ProductAdapter(products)

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