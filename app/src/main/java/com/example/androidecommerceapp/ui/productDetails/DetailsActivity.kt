package com.example.androidecommerceapp.ui.productDetails

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.database.CartEntity
import com.example.androidecommerceapp.database.ProductEntity
import com.example.androidecommerceapp.databinding.ActivityDetailsBinding
import com.example.androidecommerceapp.ui.myCart.MyCartViewModel
import com.example.androidecommerceapp.ui.productList.ProductListActivity
import com.example.androidecommerceapp.ui.shareDetails.ShareActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private lateinit var tv_title: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_description: TextView
    private lateinit var imageView: ImageView


    private val detailsViewModel: DetailsViewModel by viewModels()
    private val cartViewModel: MyCartViewModel by viewModels()

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
                price = product.price
            )
            // Add product to cart
            cartViewModel.addToCart(cartItem)

            Toast.makeText(applicationContext, "Product added to cart", Toast.LENGTH_SHORT).show()

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


}