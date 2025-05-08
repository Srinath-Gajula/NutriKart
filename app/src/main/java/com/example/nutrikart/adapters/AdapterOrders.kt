package com.example.nutrikart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrikart.R
import com.example.nutrikart.databinding.ItemViewOrdersBinding
import com.example.nutrikart.models.OrderedItems
import com.example.nutrikart.models.Orders

class AdapterOrders (private val context: Context) : RecyclerView.Adapter<AdapterOrders.OrdersViewHolder>() {

    class OrdersViewHolder(val binding : ItemViewOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<OrderedItems>(){
        override fun areItemsTheSame(
            oldItem: OrderedItems,
            newItem: OrderedItems
        ): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(
            oldItem: OrderedItems,
            newItem: OrderedItems
        ): Boolean {
            return oldItem == newItem
        }

    }



    val differ = AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersViewHolder {
        return OrdersViewHolder(ItemViewOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: OrdersViewHolder,
        position: Int
    ) {
        val order = differ.currentList[position]
        holder.binding.apply {
            tvOrderTitles.text = order.itemTitle
            tvOrderDate.text = order.itemDate
            tvOrderAmount.text = order.itemPrice.toString()

//            when(order.itemStatus){
//                0 -> {
//                    tvOrderStatus.text = "Ordered"
//                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(context , R.color.dark_gray)
//                }
//                1 -> {
//                    tvOrderStatus.text = "Received"
//                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(context , R.color.cream)
//
//                }2 -> {
//                    tvOrderStatus.text = "Dispatched"
//                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(context , R.color.Mint_Green)
//
//                }3 -> {
//                    tvOrderStatus.text = "Delivered"
//                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(context , R.color.green)
//
//                }
//
//            }

            when(order.itemStatus) {
                0 -> {
                    tvOrderStatus.text = "Ordered"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.dark_gray)
                }
                1 -> {
                    tvOrderStatus.text = "Packed"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.cream)
                }
                2 -> {
                    tvOrderStatus.text = "Shipped"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.Mint_Green)
                }
                3 -> {
                    tvOrderStatus.text = "Delivered"
                    tvOrderStatus.backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.green)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

//    class OrdersViewHolder(val binding : ItemViewOrdersBinding) : RecyclerView.ViewHolder(binding.root)
}