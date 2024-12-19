package com.example.androidecommerceapp.view.productDetails

import android.os.Bundle
import android.util.Log
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
import android.view.WindowManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.utils.OrderStatusUpdateWorker
import com.example.androidecommerceapp.view.dataModel.Product
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.databinding.ActivityDetailsBinding
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

        // Retrieve the Product object from database
        val product: Product? = intent.getSerializableExtra("PRODUCT") as? Product

        // Retrieve the Product object from api

        val productEntity: ProductEntity? = intent.getSerializableExtra("PRODUCT") as? ProductEntity


        if (product != null) {
            // If Product is not null, set its data
            tv_price.text = "$ ${product.price}"
            tv_title.text = product.title
            tv_description.text = product.description
            Glide.with(this).load(product.image).into(imageView)

            Log.d("id---->", "${product.id}")

            // Observe the favorite status
            detailsViewModel.isFavorite.observe(this) { isFavorite ->
                if (isFavorite) {
                    binding.favoritesButton.setImageResource(R.drawable.ic_favorite)
                } else {
                    binding.favoritesButton.setImageResource(R.drawable.ic_favorite_border)
                }
            }

            // Check if the product is already in the favorites when the activity starts
            detailsViewModel.checkIfFavorite(product.id)

            binding.favoritesButton.setOnClickListener { button ->
                val productEntity = ProductEntity(
                    id = product.id,
                    title = product.title,
                    description = product.description,
                    image = product.image,
                    price = product.price
                )
                if (detailsViewModel.isFavorite.value == true) {
                    detailsViewModel.removeFromFavorites(productEntity)
                    Toast.makeText(
                        applicationContext,
                        "Product removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Manually update the LiveData to reflect the "not favorite" state
//                    detailsViewModel.setFavoriteState(false)
                } else {
                    detailsViewModel.addToFavorites(productEntity)
                    Toast.makeText(
                        applicationContext,
                        "Product added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Manually update the LiveData to reflect the "favorite" state
//                    detailsViewModel.setFavoriteState(true)

                }
            }
        } else if (productEntity != null) {
            // If ProductEntity is not null, set its data
            tv_price.text = "$ ${productEntity.price}"
            tv_title.text = productEntity.title
            tv_description.text = productEntity.description
            Glide.with(this).load(productEntity.image).into(imageView)

            Log.d("id---->", "${productEntity.id}")

            // Observe the favorite status
            detailsViewModel.isFavorite.observe(this) { isFavorite ->
                binding.favoritesButton.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
            }

            // Same logic for favorites button
            detailsViewModel.checkIfFavorite(productEntity.id)
            binding.favoritesButton.setOnClickListener {
                if (detailsViewModel.isFavorite.value == true) {
                    detailsViewModel.removeFromFavorites(productEntity)
                    Toast.makeText(
                        applicationContext,
                        "Product removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
//                    detailsViewModel.setFavoriteState(false)

                } else {
                    detailsViewModel.addToFavorites(productEntity)
                    Toast.makeText(
                        applicationContext,
                        "Product added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
//                    detailsViewModel.setFavoriteState(true)

                }
            }
        } else {
            // Handle the case where neither Product nor ProductEntity is provided
            Toast.makeText(this, "No product data found", Toast.LENGTH_SHORT).show()
            finish()
        }
        detailsViewModel.getFavorites()

//        // Observe the favorite products
//        detailsViewModel.favorites.observe(this) { favorites ->
//            Log.d("Favorites", "Favorites count: ${favorites.size}")
//        }

        // Share button click listener
        binding.btnShare.setOnClickListener {
            val intent = Intent(this, ShareActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddtoCart.setOnClickListener {
            val cartItem = CartEntity(
                id = product!!.id,
                title = product.title,
                description = product.description,
                image = product.image,
                price = product.price,
                quantity = 1
            )
            // Add product to cart
            cartViewModel.addToCart(cartItem)

            Toast.makeText(applicationContext, "Product added to cart", Toast.LENGTH_SHORT).show()

        }
        binding.btnBuyNow.setOnClickListener {
            val orderItem = OrderEntity(
                title = product!!.title,
                description = product.description,
                image = product.image,
                price = product.price,
                status = "Processing",
                id = product.id,
                quantity = 1,
                orderDate = System.currentTimeMillis()
            )

            // Add product to orders
            orderHistoryViewModel.addOrder(orderItem)

            // Get the orderId for the scheduled WorkManager tasks
            val orderId = orderItem.id

            // Schedule WorkManager tasks for order status updates after specific intervals
            scheduleOrderStatusUpdates(orderId)

            Toast.makeText(applicationContext, "Product added to orders", Toast.LENGTH_SHORT).show()
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
        detailsViewModel.initializeFavorites() // Ensure the favorite state is refreshed
    }

}