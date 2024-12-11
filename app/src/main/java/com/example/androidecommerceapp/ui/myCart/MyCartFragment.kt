package com.example.androidecommerceapp.ui.myCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.CartProduct
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.databinding.FragmentMyCartBinding
import com.example.androidecommerceapp.ui.adapter.CartProductAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyCartFragment : Fragment() {

    private var _binding: FragmentMyCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartProductAdapter
    private val cartItems = mutableListOf<CartEntity>()
    private val cartViewModel: MyCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        cartAdapter = CartProductAdapter(cartItems, { cartItem ->
            cartViewModel.removeFromCart(cartItem)
        }, { cartItem ->
            cartViewModel.updateCartItem(cartItem)
        })

        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCart.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.recyclerViewCart.visibility = View.GONE
                binding.emptyCartMessage.visibility = View.VISIBLE
                cartAdapter.notifyDataSetChanged()
            } else {
                binding.recyclerViewCart.visibility = View.VISIBLE
                binding.emptyCartMessage.visibility = View.GONE
                cartItems.clear()
                cartItems.addAll(items)
                cartAdapter.notifyDataSetChanged()
            }
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
//            binding.tvTotalPrice.text = "Total: $${total}"
            // Format the total price to show 2 decimal places
            val formattedTotal = String.format("%.2f", total)

            // Update the TextView with the formatted total
            binding.tvTotalPrice.text = "Total: $$formattedTotal"

        }

        cartViewModel.getCartItems()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}