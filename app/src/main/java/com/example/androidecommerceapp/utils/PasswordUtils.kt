package com.example.androidecommerceapp.utils


import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import com.example.androidecommerceapp.R


object PasswordUtils {

    // Extension function to toggle password visibility
    fun togglePasswordVisibility(editText: EditText, imageView: ImageView, isPasswordVisible: Boolean): Boolean {
        // Toggle the password visibility state
        val newVisibilityState = !isPasswordVisible

        // Set the input type for the EditText (password visibility)
        val inputType = if (newVisibilityState) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL  // Show password
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD  // Hide password
        }
        editText.inputType = inputType

        // Set the eye icon based on the visibility state
        val icon = if (newVisibilityState) {
            R.drawable.ic_eye_on
        } else {
            R.drawable.ic_eye_off
        }
        imageView.setImageResource(icon)

        // Move the cursor to the end of the password field after toggling visibility
        editText.setSelection(editText.text.length)

        // Return the new visibility state
        return newVisibilityState
    }
}
