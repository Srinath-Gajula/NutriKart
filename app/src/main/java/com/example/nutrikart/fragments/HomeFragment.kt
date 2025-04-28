package com.example.nutrikart.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.Constants
import com.example.nutrikart.R
import com.example.nutrikart.adapters.AdapterCategory
import com.example.nutrikart.databinding.FragmentHomeBinding
import com.example.nutrikart.models.Category
import com.example.nutrikart.viewmodels.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater)

        setStatusBarColor()
        setAllCategories()
        navigatingToSearchFragment()
        onProfileClicked()
//        get()

        return binding.root
    }

    private fun onProfileClicked() {
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun navigatingToSearchFragment() {
        binding.searchCv.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()

        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(
                Category(
                    Constants.allProductsCategory[i],
                    Constants.allProductsCategoryIcon[i]
                )
            )
        }

        binding.rvCategories.adapter = AdapterCategory(categoryList , ::onCategoryIconClicked)

    }

    fun onCategoryIconClicked(category: Category) {
        val bundle = Bundle()
        bundle.putString("category" , category.title)
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment , bundle)
    }

//    private fun get(){
//        viewModel.getAll().observe(viewLifecycleOwner){
//            for (i in it){
//                Log.d("vvv" , i.productTitle.toString())
//                Log.d("vvv" , i.productCount.toString())
//            }
//        }
//    }


    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.green)
            this.statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }



}