package com.example.androidecommerceapp.ui.favorites

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
import com.example.androidecommerceapp.databinding.FragmentFavoritesBinding
import com.example.androidecommerceapp.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavoritesMessage: TextView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteItems = mutableListOf<FavoriteItem>()

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var tv_title: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tv_title = binding.tvWhyy

        // Initialize views
        recyclerView = binding.recyclerViewFavorites
        emptyFavoritesMessage = binding.emptyFavoritesMessage

        // Set up the RecyclerView and the adapter
        favoriteAdapter = FavoriteAdapter(favoriteItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteAdapter

        loadFavorites() // Load the list of favorites (can be from API or database)

        // Observe the favorite products
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->

            val favoriteIds = favorites.map { it.id }
            tv_title.text = favoriteIds.toString()
        }

        // Fetch favorites when the fragment is created
        favoritesViewModel.getFavorites()


        return root
    }

    private fun loadFavorites() {
        // Example data (replace with actual data loading logic from database, API, etc.)
        val sampleFavorites = listOf(
            FavoriteItem("Product 1", 29.99, R.drawable.shoe2),
            FavoriteItem("Product 2", 49.99, R.drawable.shoe2),
            FavoriteItem("Product 3", 19.99, R.drawable.shoe2)
        )

        if (sampleFavorites.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyFavoritesMessage.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyFavoritesMessage.visibility = View.GONE
        }

        // Set the data to the adapter
        favoriteItems.clear()  // Clear previous data
        favoriteItems.addAll(sampleFavorites)
        favoriteAdapter.notifyDataSetChanged()
    }


    fun updateEmptyState() {
        if (favoriteItems.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyFavoritesMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}