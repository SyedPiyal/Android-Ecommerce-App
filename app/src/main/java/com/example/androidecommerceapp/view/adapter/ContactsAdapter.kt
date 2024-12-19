package com.example.androidecommerceapp.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.view.dataModel.Contact
import com.example.androidecommerceapp.databinding.ItemContactBinding


class ContactsAdapter(private val mContext: Context, private val contacts: List<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.contactName.text = contact.name
//            binding.contactPhone.text = contact.phoneNumber

                // on click event
            itemView.setOnClickListener {
                // Show a Toast message when the item is clicked
                Toast.makeText(mContext, "Share with: ${contact.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}