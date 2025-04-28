package com.example.nutrikart.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrikart.databinding.ItemViewOrdersBinding

class AdapterOrders : RecyclerView.Adapter<AdapterOrders.OrdersViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: OrdersViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class OrdersViewHolder(val binding : ItemViewOrdersBinding) : RecyclerView.ViewHolder(binding.root)
}