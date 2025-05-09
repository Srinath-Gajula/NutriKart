package com.example.nutrikart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.R
import com.example.nutrikart.adapters.AdapterOrders
import com.example.nutrikart.databinding.FragmentOrdersBinding
import com.example.nutrikart.models.OrderedItems
import com.example.nutrikart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var adapterOrders: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrdersBinding.inflate(layoutInflater)

        onBackButtonClicked()
        getAllOrders()

        return binding.root
    }

    private fun getAllOrders() {
        lifecycleScope.launch {
            viewModel.getAllOrders().collect {orderList ->

                if(orderList.isNotEmpty()){
                    val orderedList = ArrayList<OrderedItems>()
                    for (orders in orderList){

                        val title = StringBuilder()
                        var totalPrice = 0

                        for (products in orders.orderList!!){
                            val price = products.productPrice?.substring(1)?.toInt()  //₹24
                            val itemCount = products.productCount!!
                            totalPrice += (price?.times(itemCount)!!)

                            title.append("${products.productCategory}, ")
                        }

                        val orderedItems = OrderedItems(orders.orderId , orders.orderDate , orders.orderStatus, title.toString(), totalPrice)
                        orderedList.add(orderedItems)

                    }
                    adapterOrders = AdapterOrders(requireContext() , ::onOrderItemViewClicked)
                    binding.rvOrders.adapter = adapterOrders
                    adapterOrders.differ.submitList(orderedList)
                }
            }
        }

    }

    fun onOrderItemViewClicked(orderedItems: OrderedItems){
        val bundle = Bundle()
        bundle.putInt("status" , orderedItems.itemStatus!!)
        bundle.putString("orderId" , orderedItems.orderId)

        findNavController().navigate(R.id.action_ordersFragment_to_orderDetailFragment , bundle)

    }

    private fun onBackButtonClicked() {
        binding.tbProfileFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_ordersFragment_to_profileFragment)
        }
    }


}