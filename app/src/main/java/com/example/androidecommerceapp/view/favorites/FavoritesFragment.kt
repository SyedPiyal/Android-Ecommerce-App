package com.example.androidecommerceapp.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.databinding.FragmentFavoritesBinding
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.adapter.FavoriteAdapter
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.view.favorites.viewModel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavoritesMessage: TextView
    private lateinit var favoriteAdapter: FavoriteAdapter

    private val favoritesViewModel: FavoritesViewModel by viewModels()

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

        favoritesViewModel.fetchFavoriteProductIds()

        favoritesViewModel.favoriteEntityIds.observe(viewLifecycleOwner, Observer { favoriteIds ->
            // Create a list to hold the product details
            val productDetailsList = mutableListOf<Product>()

            // Fetch product details for each favorite ID
            viewLifecycleOwner.lifecycleScope.launch {
                favoriteIds.forEach { favoriteProduct ->
                    favoritesViewModel.getProductById(favoriteProduct.productId).collect { result ->
                        when (result) {
                            is ResultState.Success -> {
                                productDetailsList.add(result.data)
                                if (productDetailsList.size == favoriteIds.size) {
                                    // Update the RecyclerView adapter when all products are fetched
                                    favoriteAdapter =
                                        FavoriteAdapter(requireContext(), productDetailsList)
                                    binding.recyclerViewFavorites.adapter = favoriteAdapter
                                }
                            }

                            is ResultState.Error -> {
                                // Handle error
                                Toast.makeText(
                                    context,
                                    "Error: ${result.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            ResultState.Loading -> {

                            }
                        }
                    }
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
