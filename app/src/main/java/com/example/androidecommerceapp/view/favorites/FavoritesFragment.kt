package com.example.androidecommerceapp.view.favorites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.databinding.FragmentFavoritesBinding
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.adapter.FavoriteAdapter
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.productDetails.DetailsActivity
import com.example.androidecommerceapp.view.productDetails.viewModel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavoritesMessage: TextView
    private lateinit var favoriteAdapter: FavoriteAdapter

    private val favoritesViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize views
        recyclerView = binding.recyclerViewFavorites
        emptyFavoritesMessage = binding.emptyFavoritesMessage

        // Initialize the FavoritesAdapter with remove button action
        favoriteAdapter = FavoriteAdapter(
            onRemoveClickListener = { productId ->
                // Handle removing product from favorites
                favoritesViewModel.removeProductFromFavorites(productId)
            },
            onItemClick = { productId ->
                // Handle item click - start DetailsActivity and pass the product id
                val intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtra("PRODUCT", productId) // Pass the product id
                startActivity(intent)
            }
        )

        // Setup RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteAdapter


        // Observe the favorite products
        favoritesViewModel.favoriteProducts.observe(viewLifecycleOwner, Observer { products ->
            products?.let {
                if (it.isEmpty()) {
                    // Show empty favorites message if no products
                    emptyFavoritesMessage.visibility = View.VISIBLE
                } else {
                    Log.d("Favo Data --->", it.toString())
                    // Hide empty favorites message if products exist
                    emptyFavoritesMessage.visibility = View.GONE
                    favoriteAdapter.submitList(it)
                }

            }
        })

        // Load favorite products
        favoritesViewModel.loadFavorites()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
