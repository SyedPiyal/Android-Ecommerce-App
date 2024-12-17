package com.example.androidecommerceapp.ui.profile


import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.dataModel.ProfileOption
import com.example.androidecommerceapp.databinding.FragmentProfileBinding
import com.example.androidecommerceapp.ui.adapter.ProfileOptionAdapter
import com.example.androidecommerceapp.ui.auth.LoginActivity
import com.example.androidecommerceapp.ui.editProfile.EditProfileActivity
import com.example.androidecommerceapp.ui.orderHistory.OrderHistoryActivity
import com.example.androidecommerceapp.ui.paymentMethod.PaymentMethodActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set up profile info (hardcoded for now)
        binding.tvProfileName.text = "John Doe"
        binding.tvProfileEmail.text = "johndoe@example.com"

        // Profile options
        val profileOptions = listOf(
            ProfileOption(R.drawable.ic_edit, "Edit Profile"),
            ProfileOption(R.drawable.ic_order_history, "Order History"),
            ProfileOption(R.drawable.ic_payment, "Payment Methods"),
            ProfileOption(R.drawable.ic_logout, "Logout")
        )

        val profileOptionAdapter = ProfileOptionAdapter(profileOptions) { option ->
            // Handle option click
            when (option.title) {
                "Edit Profile" -> {
                    val intent = Intent(requireContext(), EditProfileActivity::class.java)
                    startActivity(intent)
                }

                "Order History" -> {
                    val intent = Intent(requireContext(), OrderHistoryActivity::class.java)
                    startActivity(intent)
                }

                "Payment Methods" -> {
                    val intent = Intent(requireContext(), PaymentMethodActivity::class.java)
                    startActivity(intent)
                }

                "Logout" -> {
                    showCustomLogoutDialog()
                }
            }
        }

        binding.rvProfileOptions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = profileOptionAdapter
        }
        return binding.root
    }

    private fun showCustomLogoutDialog() {
        // Inflate the custom layout for the dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)

        // Get references to the buttons in the custom dialog
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)

        // Create an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)  // Set the custom layout as the dialog's view
            .setCancelable(true)   // Allow the dialog to be canceled by tapping outside

        // Create the dialog
        val customDialog = dialogBuilder.create()

        // Set button actions
        btnCancel.setOnClickListener {
            customDialog.dismiss()  // Dismiss the dialog when the cancel button is clicked
        }

        btnConfirm.setOnClickListener {
            customDialog.dismiss()  // Dismiss the dialog
            performLogout()

        }

        // Show the dialog
        customDialog.show()
    }

    private fun performLogout() {
        // Clear SharedPreferences data on logout
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()  // Clears all the saved preferences (user data, login state, etc.)
        editor.apply()


        Toast.makeText(requireContext(), "Log out", Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}