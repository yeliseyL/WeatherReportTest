package com.eliseylobanov.weatherreporttest.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliseylobanov.weatherreporttest.databinding.MainRecyclerItemBinding
import com.eliseylobanov.weatherreporttest.model.City

class CitiesAdapter(private val onClickListener: OnClickListener) :
        ListAdapter<City, CitiesAdapter.RecipesViewHolder>(DiffCallback) {

    class RecipesViewHolder(private var binding: MainRecyclerItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(city: City) {
            binding.city = city
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecipesViewHolder {
        return RecipesViewHolder(MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val city = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(city)
        }
        holder.bind(city)
    }

    class OnClickListener(val clickListener: (city: City) -> Unit) {
        fun onClick(city: City) = clickListener(city)
    }
}