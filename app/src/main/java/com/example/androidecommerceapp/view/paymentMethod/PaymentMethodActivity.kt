package com.example.androidecommerceapp.view.paymentMethod

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.view.dataModel.PaymentMethod
import com.example.androidecommerceapp.databinding.ActivityPaymentMethodBinding
import com.example.androidecommerceapp.view.adapter.PaymentMethodAdapter

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var paymentMethodsList: List<PaymentMethod>
    private lateinit var binding: ActivityPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorSelected)


        // Initialize RecyclerView
        binding.recyclerViewPaymentMethods.layoutManager = LinearLayoutManager(this)


        paymentMethodsList = getSavedPaymentMethods()

        if (paymentMethodsList.isEmpty()) {
            binding.emptyStateMessage.visibility = View.VISIBLE
            binding.recyclerViewPaymentMethods.visibility = View.GONE
        } else {
            paymentMethodAdapter = PaymentMethodAdapter(paymentMethodsList)
            binding.recyclerViewPaymentMethods.adapter = paymentMethodAdapter
            binding.emptyStateMessage.visibility = View.GONE
            binding.recyclerViewPaymentMethods.visibility = View.VISIBLE
        }

        // Handle the "Add Payment Method" button click
        binding.btnAddPaymentMethod.setOnClickListener {

            showAddPaymentMethodDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getSavedPaymentMethods(): List<PaymentMethod> {
        return listOf(
            PaymentMethod("Visa **** 1234", "Credit Card", "12/24"),
            PaymentMethod("PayPal", "PayPal", "N/A")
        )
    }

    private fun showAddPaymentMethodDialog() {
        // Placeholder for adding a new payment method
        // Example: StartActivityForResult or a simple dialog
    }
}