package com.example.androidecommerceapp.utils


import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.androidecommerceapp.R



object ToastUtils {


    fun showCustomToast(context: Context, message: String, type: ToastTypeM) {
        // Inflate the custom toast layout
        val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
        val toastMessage = layout.findViewById<TextView>(R.id.toast_message)
        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)


        toastMessage.text = message


        when (type) {
            ToastTypeM.SUCCESS -> {
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.success_yellow))
                toastIcon.setImageResource(R.drawable.ic_check)
            }
            ToastTypeM.ERROR -> {
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.error_red))
                toastIcon.setImageResource(R.drawable.ic_error)
            }
        }

        // Create and show the custom toast
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}
