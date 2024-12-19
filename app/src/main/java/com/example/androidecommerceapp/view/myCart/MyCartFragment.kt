package com.example.androidecommerceapp.view.myCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.databinding.FragmentMyCartBinding
import com.example.androidecommerceapp.view.adapter.CartProductAdapter
import com.example.androidecommerceapp.view.orderHistory.viewModel.OrderHistoryViewModel
import com.example.androidecommerceapp.utils.OrderStatusScheduler
import com.example.androidecommerceapp.utils.OrderStatusScheduler.scheduleOrderStatusUpdates
import com.example.androidecommerceapp.view.myCart.viewModel.MyCartViewModel
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
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    private var totalPriceDat: Double = 0.0
    private var totalQuantityDat: Int = 0

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
            } else {
                binding.recyclerViewCart.visibility = View.VISIBLE
                binding.emptyCartMessage.visibility = View.GONE
                cartItems.clear()
                cartItems.addAll(items)
                cartAdapter.notifyDataSetChanged()
            }
        }

        cartViewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            totalPriceDat=total
//            binding.tvTotalPrice.text = "Total: $${total}"
            // Format the total price to show 2 decimal places
            val formattedTotal = String.format("%.2f", total)

            // Update the TextView with the formatted total
            binding.tvTotalPrice.text = "Total: $$formattedTotal"

        }

        cartViewModel.totalQuantity.observe(viewLifecycleOwner) { totalQ ->

            // Update the TextView with the formatted total
//            binding.tvTotalQuantity.text = "Total Quantity: $totalQ"
            totalQuantityDat = totalQ

        }

        cartViewModel.getCartItems()

        binding.btnBuyNow.setOnClickListener {
            val orderItem = OrderEntity(
                title = "Shoe",
                description = "description",
                image = "image",
                price = totalPriceDat,
                status = "Processing",
                id = 100,
                quantity = totalQuantityDat, // You can adjust this to handle quantity input if needed
                orderDate = System.currentTimeMillis() // Timestamp for when the order was placed
            )
            // Add product to orders
            orderHistoryViewModel.addOrder(orderItem)

            // Get the orderId for the scheduled WorkManager tasks
            val orderId = orderItem.id

            // Schedule WorkManager tasks for order status updates after specific intervals
            scheduleOrderStatusUpdates(orderId)


            Toast.makeText(requireContext(), "Product added to orders", Toast.LENGTH_SHORT).show()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}