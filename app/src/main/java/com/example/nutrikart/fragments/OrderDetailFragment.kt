package com.example.nutrikart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.R
import com.example.nutrikart.adapters.AdapterCartProducts
import com.example.nutrikart.adapters.AdapterOrders
import com.example.nutrikart.databinding.FragmentOrderDetailBinding
import com.example.nutrikart.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderDetailFragment : Fragment() {

    private val viewModel : UserViewModel by viewModels()
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var orderId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        getValues()
        settingStatus()
        onBackButtonClicked()
        lifecycleScope.launch { getOrderedProducts() }
        return binding.root
    }


    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_ordersFragment)
        }
    }

    suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect{cartList ->
            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)

        }

    }

    private fun settingStatus() {
//        when(status){
//            0 -> {
//                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//            }
//            1 -> {
//                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//            }
//            2 -> {
//                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//            }
//            3 -> {
//                binding.iv1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view1.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv3.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view2.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.iv4.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//                binding.view3.backgroundTintList = ContextCompat.getColorStateList(requireContext() , R.color.green)
//            }
//
//        }

        val views = listOf(
            binding.iv1,
            binding.iv2,
            binding.view1,
            binding.iv3,
            binding.view2,
            binding.iv4,
            binding.view3
        )

        val greenColor = ContextCompat.getColorStateList(requireContext(), R.color.green)

        // Apply green tint to the required number of views based on status
        for (i in 0 until status * 2 + 1) {
            if (i < views.size) {
                views[i].backgroundTintList = greenColor
            }
        }
    }

    private fun getValues() {
        val bundle = arguments
        status = bundle?.getInt("status")!!
        orderId = bundle.getString("orderId").toString()
    }


}