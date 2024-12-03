package com.example.androidecommerceapp.ui.productDetails

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.Color
import com.example.androidecommerceapp.dataModel.ShoeSize
import com.example.androidecommerceapp.databinding.ActivityProductDetailsBinding
import com.example.androidecommerceapp.ui.adapter.ColorAdapter
import com.example.androidecommerceapp.ui.adapter.SizeAdapter

class ProductDetailsActivity : AppCompatActivity() {

    private var quantity = 1
    private lateinit var quantityText: TextView
    private lateinit var incrementButton: Button
    private lateinit var decrementButton: Button

    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.sizeRecyclerView

        // Create a list of shoe sizes
        val sizes = listOf(
            ShoeSize("5"), ShoeSize("6"), ShoeSize("7"), ShoeSize("8"), ShoeSize("9"),
        )

        // Set up the RecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = SizeAdapter(sizes)

        // Set up Color RecyclerView
        val colorRecyclerView = binding.colorRecyclerView
        val colors = listOf(
            Color("Red", android.R.color.holo_red_dark),
            Color("Blue", android.R.color.holo_blue_dark),
            Color("Green", android.R.color.holo_green_dark),
        )
        colorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorRecyclerView.adapter = ColorAdapter(colors)

        // Initialize the views
        quantityText = findViewById(R.id.quantityText)
        incrementButton = findViewById(R.id.incrementButton)
        decrementButton = findViewById(R.id.decrementButton)

        // Set up listeners for the buttons
        incrementButton.setOnClickListener {
            // Increment quantity
            quantity++
            updateQuantityText()
        }

        decrementButton.setOnClickListener {
            // Decrement quantity, ensuring it doesn't go below 1
            if (quantity > 1) {
                quantity--
                updateQuantityText()
            }
        }

//        // Set up Quantity Picker
//        val quantityPicker = binding.quantityPicker
//        quantityPicker.minValue = 1
//        quantityPicker.maxValue = 10


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Helper function to update the quantity display
    private fun updateQuantityText() {
        quantityText.text = quantity.toString()
    }
}