package com.example.nutrikart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutrikart.databinding.ItemViewCartProductsBinding
import com.example.nutrikart.roomdb.CartProductTable


class AdapterCartProducts : RecyclerView.Adapter<AdapterCartProducts.CartProductsViewHolder>() {

    class CartProductsViewHolder(val binding : ItemViewCartProductsBinding) : RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<CartProductTable>(){
        override fun areItemsTheSame(
            oldItem: CartProductTable,
            newItem: CartProductTable
        ): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(
            oldItem: CartProductTable,
            newItem: CartProductTable
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this , diffUtil)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductsViewHolder {
        return CartProductsViewHolder(ItemViewCartProductsBinding.inflate(LayoutInflater.from(parent.context) , parent, false))
    }

    override fun onBindViewHolder(
        holder: CartProductsViewHolder,
        position: Int
    ) {
        val product = differ.currentList[position]

        holder.binding.apply {
            Glide.with(holder.itemView).load(product.productImage).into(ivProductImage)
            tvProductTittle.text = product.productTitle
            tvProductQuantity.text = product.productQuantity
            tvProductPrice.text = product.productPrice
            tvProductCount.text = product.productCount.toString()
        }
 
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}