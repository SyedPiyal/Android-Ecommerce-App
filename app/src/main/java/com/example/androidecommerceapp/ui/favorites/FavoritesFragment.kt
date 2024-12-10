package com.example.androidecommerceapp.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.FavoriteItem
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.databinding.FragmentFavoritesBinding
import com.example.androidecommerceapp.ui.adapter.FavoriteAdapter
import com.example.androidecommerceapp.ui.productDetails.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavoritesMessage: TextView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteItems = mutableListOf<ProductEntity>() // Store the fetched products

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

        // Set up RecyclerView and adapter
        favoriteAdapter = FavoriteAdapter(favoriteItems) { selectedProduct ->
            val intent = Intent(requireContext(), DetailsActivity::class.java).apply {
                putExtra("PRODUCT", selectedProduct) // Pass the selected product
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteAdapter

        // Fetch favorites from ViewModel
        favoritesViewModel.getFavorites()

        // Observe the favorites list from ViewModel
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyFavoritesMessage.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyFavoritesMessage.visibility = View.GONE
                // Update the adapter's data
                favoriteItems.clear()
                favoriteItems.addAll(favorites)
                favoriteAdapter.notifyDataSetChanged()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
