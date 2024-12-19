package com.example.androidecommerceapp.view.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.R
import com.example.androidecommerceapp.view.dataModel.PaymentMethod


class PaymentMethodAdapter(private val paymentMethodsList: List<PaymentMethod>) :
    RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val paymentMethod = paymentMethodsList[position]
        holder.bind(paymentMethod)
    }

    override fun getItemCount(): Int = paymentMethodsList.size

    class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Using findViewById to access the views
        private val tvMethod = itemView.findViewById<TextView>(R.id.tvMethod)
        private val tvType = itemView.findViewById<TextView>(R.id.tvType)
        private val tvExpiry = itemView.findViewById<TextView>(R.id.tvExpiry)

        fun bind(paymentMethod: PaymentMethod) {
            tvMethod.text = paymentMethod.method
            tvType.text = paymentMethod.type
            tvExpiry.text = paymentMethod.expiry
        }
    }
}
