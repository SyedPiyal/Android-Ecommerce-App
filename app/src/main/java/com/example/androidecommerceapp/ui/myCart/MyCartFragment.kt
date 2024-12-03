package com.example.androidecommerceapp.ui.myCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.CartProduct
import com.example.androidecommerceapp.databinding.FragmentMyCartBinding
import com.example.androidecommerceapp.ui.adapter.CartProductAdapter


class MyCartFragment : Fragment() {

    private var _binding: FragmentMyCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Sample demo data for cart products
        val demoData = listOf(
            CartProduct(
                image = R.drawable.shoe2,
                name = "Adidas Predator",
                price = "$50",
                quantity = 1,
                color = R.color.g_blue,
                size = "L"
            ), CartProduct(
                image = R.drawable.shoe2,
                name = "Adidas Python",
                price = "$100",
                quantity = 1,
                color = R.color.g_red,
                size = "M"
            )
        )

        // Set up RecyclerView
        val cartProductAdapter = CartProductAdapter(demoData)
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartProductAdapter
        }


        if (demoData.isEmpty()) {
            binding.layoutCartEmpty.visibility = View.VISIBLE
            binding.rvCart.visibility = View.GONE
        } else {
            binding.layoutCartEmpty.visibility = View.GONE
            binding.rvCart.visibility = View.VISIBLE
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}