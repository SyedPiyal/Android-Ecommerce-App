package com.example.androidecommerceapp.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidecommerceapp.view.dataModel.ProfileOption
import com.example.androidecommerceapp.databinding.ItemProfileOptionBinding


class ProfileOptionAdapter(
    private val optionsList: List<ProfileOption>, private val onItemClick: (ProfileOption) -> Unit
) : RecyclerView.Adapter<ProfileOptionAdapter.ProfileOptionViewHolder>() {

    inner class ProfileOptionViewHolder(val binding: ItemProfileOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(option: ProfileOption) {
            binding.icon.setImageResource(option.icon)
            binding.title.text = option.title
            itemView.setOnClickListener { onItemClick(option) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileOptionViewHolder {
        val binding =
            ItemProfileOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileOptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileOptionViewHolder, position: Int) {
        holder.bind(optionsList[position])
    }

    override fun getItemCount(): Int = optionsList.size
}
