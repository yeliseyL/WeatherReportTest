package com.eliseylobanov.weatherreporttest.ui

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliseylobanov.weatherreporttest.model.City
import com.eliseylobanov.weatherreporttest.ui.main.CitiesAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<City>?) {
    val adapter = recyclerView.adapter as CitiesAdapter
    adapter.submitList(data)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("temperature")
fun bindTemperature(textView: TextView, temp: Double) {
    textView.text = temp.toInt().toString() + "\u00B0"
}