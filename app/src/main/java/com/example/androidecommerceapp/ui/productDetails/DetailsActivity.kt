package com.example.androidecommerceapp.ui.productDetails

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Product
import com.example.androidecommerceapp.databinding.ActivityDetailsBinding
import com.example.androidecommerceapp.databinding.ItemCategoryBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private lateinit var tv_title: TextView
    private lateinit var tv_price: TextView
    private lateinit var tv_description: TextView
    private lateinit var imageView: ImageView

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

        // Use the retrieved product object as needed
        product?.let {
            tv_price.text = it.price.toString()
            tv_title.text = it.title
            tv_description.text = it.description
            Glide.with(this).load(it.image)  // Assuming imageUrl is a URL string
                .into(imageView)

            // Use the product details
            Log.d("ProductDetails", "Product: ${it.title}")

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}