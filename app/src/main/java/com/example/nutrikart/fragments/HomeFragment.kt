package com.example.nutrikart.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.Constants
import com.example.nutrikart.LocaleHelper
import com.example.nutrikart.R
import com.example.nutrikart.adapters.AdapterCategory
import com.example.nutrikart.databinding.FragmentHomeBinding
import com.example.nutrikart.models.Category
import com.example.nutrikart.viewmodels.UserViewModel
import java.net.URLEncoder


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
        openWhatsApp()
        setupLanguageSpinner()

//        get()

        return binding.root
    }

    private fun setupLanguageSpinner() {
        val languageCodes = listOf("en", "te", "kn", "hi")

        fun getSavedLangIndex(): Int {
            val savedLang = LocaleHelper.getLanguage(requireContext())
            val languageCodes = listOf("en", "te", "kn", "hi")
            return languageCodes.indexOf(savedLang).takeIf { it >= 0 } ?: 0
        }

        // Set default language in the spinner based on saved language
        binding.languageSpinner.setSelection(getSavedLangIndex())

        binding.languageSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLangCode = languageCodes[position]
                val currentLang = LocaleHelper.getLanguage(requireContext())

                if (selectedLangCode != currentLang) {
                    // Save the selected language
                    LocaleHelper.saveLanguage(requireContext(), selectedLangCode)

                    // Set the new locale
                    LocaleHelper.setLocale(requireContext(), selectedLangCode)

                    // Restart the app to reflect the changes
                    restartApp()
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })
    }

    private fun restartApp() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }







//    private fun openWhatsApp() {
//
//        binding.whatsappIcon.setOnClickListener {
//            val phoneNumber = "+916304097052"
//
//            try {
//                val intent = Intent(Intent.ACTION_VIEW)
//                val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode("Hello NutriKart Customer Care!", "UTF-8")
//                intent.setPackage("com.whatsapp")
//                intent.data = Uri.parse(url)
//                startActivity(intent)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    @SuppressLint("ClickableViewAccessibility")
    private fun openWhatsApp() {
        val whatsappIcon = binding.whatsappIcon

        var dX = 0f
        var dY = 0f
        var isDragging = false

        whatsappIcon.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    isDragging = false
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    val newY = event.rawY + dY

                    val parent = v.parent as View
                    val maxX = parent.width - v.width
                    val maxY = parent.height - v.height

                    // Limit within screen
                    val clampedX = newX.coerceIn(0f, maxX.toFloat())
                    val clampedY = newY.coerceIn(0f, maxY.toFloat())

                    v.animate()
                        .x(clampedX)
                        .y(clampedY)
                        .setDuration(0)
                        .start()

                    isDragging = true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        // If not dragged, treat it as a click
                        val phoneNumber = "+916304097052"
                        try {
                            val intent = Intent(Intent.ACTION_VIEW)
                            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" +
                                    URLEncoder.encode("Hello NutriKart Customer Care!", "UTF-8")
                            intent.setPackage("com.whatsapp")
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            true
        }
    }



    private fun  onProfileClicked() {
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

        }
    }



}