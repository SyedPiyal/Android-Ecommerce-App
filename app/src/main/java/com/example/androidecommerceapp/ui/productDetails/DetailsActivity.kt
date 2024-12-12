package com.example.androidecommerceapp.ui.productDetails

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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.components.OrderStatusUpdateWorker
import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.databinding.ActivityDetailsBinding
import com.example.androidecommerceapp.ui.myCart.MyCartViewModel
import com.example.androidecommerceapp.ui.orderHistory.OrderHistoryViewModel
import com.example.androidecommerceapp.ui.shareDetails.ShareActivity
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

        tv_title = binding.productTitle
        tv_price = binding.productPrice
        tv_description = binding.productDescription
        imageView = binding.productImage

        // Retrieve the Product object
        val product: Product? = intent.getSerializableExtra("PRODUCT") as? Product
        val productEntity: ProductEntity? = intent.getSerializableExtra("PRODUCT") as? ProductEntity
        // Use the retrieved product object as needed

        if (product != null) {
            // If Product is not null, set its data
            tv_price.text = "$ ${product.price}"
            tv_title.text = product.title
            tv_description.text = product.description
            Glide.with(this).load(product.image).into(imageView)

            Log.d("id---->", "${product.id}")

            binding.favoritesButton.setOnClickListener { button ->
                val productEntity = ProductEntity(
                    id = product.id,
                    title = product.title,
                    description = product.description,
                    image = product.image,
                    price = product.price
                )
                // Add product to favorites
                detailsViewModel.addToFavorites(productEntity)

                Toast.makeText(
                    applicationContext, "Product added to favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (productEntity != null) {
            // If ProductEntity is not null, set its data
            tv_price.text = "$ ${productEntity.price}"
            tv_title.text = productEntity.title
            tv_description.text = productEntity.description
            Glide.with(this).load(productEntity.image).into(imageView)

            Log.d("id---->", "${productEntity.id}")
        } else {
            // Handle the case where neither Product nor ProductEntity is provided
            Toast.makeText(this, "No product data found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if no data is provided
        }
        detailsViewModel.getFavorites()

        // Observe the favorite products
        detailsViewModel.favorites.observe(this) { favorites ->
            Log.d("Favorites", "Favorites count: ${favorites.size}")
        }


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
                quantity = 1, // You can adjust this to handle quantity input if needed
                orderDate = System.currentTimeMillis() // Timestamp for when the order was placed
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


}