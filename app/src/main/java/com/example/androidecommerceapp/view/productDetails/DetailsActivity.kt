package com.example.androidecommerceapp.view.productDetails

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.utils.OrderStatusUpdateWorker
import com.example.androidecommerceapp.databinding.ActivityDetailsBinding
import com.example.androidecommerceapp.utils.ResultState
import com.example.androidecommerceapp.view.myCart.viewModel.MyCartViewModel
import com.example.androidecommerceapp.view.orderHistory.viewModel.OrderHistoryViewModel
import com.example.androidecommerceapp.view.productDetails.viewModel.DetailsViewModel
import com.example.androidecommerceapp.view.shareDetails.ShareActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private lateinit var tv_title: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_description: TextView
    private lateinit var imageView: ImageView


    private val detailsViewModel: DetailsViewModel by viewModels()
    private val cartViewModel: MyCartViewModel by viewModels()
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()
//    private val favoriteViewModel: FavoritesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorSelected)

        tv_title = binding.productTitle
        tv_price = binding.productPrice
        tv_description = binding.productDescription
        imageView = binding.productImage

        // Retrieve the Product id
        val productId = intent.getIntExtra("PRODUCT", -1)

        // Fetch product details using the ViewModel
        if (productId != -1) {
            detailsViewModel.getProductById(productId)
        }


        // Observe product details LiveData
        detailsViewModel.productDetails.observe(this, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    // Display the product data

                    binding.progressBar.visibility = View.GONE

                    var product = result.data
                    tv_title.text = product.title
                    tv_price.text = "$${product.price}"
                    tv_description.text = product.description
                    Glide.with(this).load(product.image).into(imageView)

                    // buy now button
                    binding.btnBuyNow.setOnClickListener {
                        // Insert the OrderEntity into the Room database using the OrderHistoryViewModel
                        orderHistoryViewModel.addOrder(product)

                        // Get the orderId for the scheduled WorkManager tasks
                        val orderId = product.id

                        // Schedule WorkManager tasks for order status updates after specific intervals
                        scheduleOrderStatusUpdates(orderId)

                        // Show a toast confirming the order was placed
                        Toast.makeText(
                            applicationContext,
                            "Order placed successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // button add to cart
                    binding.btnAddtoCart.setOnClickListener {
                        cartViewModel.addToCart(product)

                        Toast.makeText(
                            applicationContext,
                            "Product added to cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        applicationContext, "Error: ${result.exception.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


        // Observe the favorite state
        detailsViewModel.isFavorite.observe(this, Observer { isFavorite ->
            // Change the button state based on whether the product is favorited
            val favoriteIcon =
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            binding.favoritesButton.setImageResource(favoriteIcon)
        })

        // add to favorites
        binding.favoritesButton.setOnClickListener {
            if (productId != -1) {
                if (detailsViewModel.isFavorite.value == true) {
                    // Product is already favorited, remove it from favorites
                    detailsViewModel.removeProductFromFavorites(productId)
                    Toast.makeText(applicationContext, "Removed from favorites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Product is not favorited, add it to favorites
                    detailsViewModel.addProductToFavorites(productId)
                    Toast.makeText(applicationContext, "Added to favorites", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

        // Share button click listener
        binding.btnShare.setOnClickListener {
            val intent = Intent(this, ShareActivity::class.java)
            startActivity(intent)
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    // Schedule WorkManager tasks to update order status and send notifications
    private fun scheduleOrderStatusUpdates(orderId: Int) {
        // Schedule WorkManager for the first status update (Shipped) after 2 minutes
        val workRequestShipped = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .addTag("01")
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Schedule WorkManager for the second status update (Delivering) after 4 minutes
        val workRequestDelivering = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Schedule WorkManager for the final status update (Delivered) after 6 minutes
        val workRequestDelivered = OneTimeWorkRequestBuilder<OrderStatusUpdateWorker>()
            .setInitialDelay(90, TimeUnit.SECONDS)
            .setInputData(workDataOf("ORDER_ID" to orderId))
            .build()

        // Enqueue WorkManager tasks
        WorkManager.getInstance(applicationContext).enqueue(workRequestShipped)
        WorkManager.getInstance(applicationContext).enqueue(workRequestDelivering)
        WorkManager.getInstance(applicationContext).enqueue(workRequestDelivered)
    }


    override fun onResume() {
        super.onResume()

    }

}