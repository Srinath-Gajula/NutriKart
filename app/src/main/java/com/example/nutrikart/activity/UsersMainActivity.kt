////package com.example.nutrikart.activity
////
////import android.content.Context
////import android.content.Intent
////import android.os.Bundle
////import android.util.Log
////import android.view.LayoutInflater
////import android.view.View
////import androidx.activity.enableEdgeToEdge
////import androidx.activity.viewModels
////import androidx.appcompat.app.AppCompatActivity
////import androidx.core.view.ViewCompat
////import androidx.core.view.WindowInsetsCompat
////import androidx.fragment.app.viewModels
////import com.example.nutrikart.CartListener
////import com.example.nutrikart.LocaleHelper
////import com.example.nutrikart.OrderPlaceActivity
////import com.example.nutrikart.R
////import com.example.nutrikart.adapters.AdapterCartProducts
////import com.example.nutrikart.databinding.ActivityUsersMainBinding
////import com.example.nutrikart.databinding.BsCartProductsBinding
////import com.example.nutrikart.roomdb.CartProductTable
////import com.example.nutrikart.viewmodels.UserViewModel
////import com.google.android.material.bottomsheet.BottomSheetDialog
////import kotlin.getValue
////import okhttp3.*
////import okhttp3.MediaType.Companion.toMediaType
////import okhttp3.RequestBody.Companion.toRequestBody
////import org.json.JSONObject
////import java.io.IOException
////class UsersMainActivity : AppCompatActivity() , CartListener{
////    private lateinit var binding : ActivityUsersMainBinding
////    private val viewModel : UserViewModel by viewModels()
////    private lateinit var cartProductList : List<CartProductTable>
//////    private var cartProductList: List<CartProductTable> = emptyList()
////    private lateinit var adapterCartProducts: AdapterCartProducts
////
////    private val client = OkHttpClient()
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = ActivityUsersMainBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////        viewModel.getAll().observe(this){
////            for (i in it){
////                fetchRecommendations(i.toString())
////                cartProductList = it
////            }
////        }
////        getAllCartProducts()
////        getTotalItemCountInCart()
////        onCartClicked()
////        onNextButtonClicked()
////    }
////    private fun fetchRecommendations(item: String) {
////        val json = JSONObject()
////        json.put("item", item)
////
////        val mediaType = "application/json; charset=utf-8".toMediaType()
////        val body = json.toString().toRequestBody(mediaType)
////
////        val request = Request.Builder()
////            .url("http://192.168.10.217:5000")  // Use the correct URL for your server
////            .post(body)
////            .build()
////
////        client.newCall(request).enqueue(object : Callback {
////            override fun onFailure(call: Call, e: IOException) {
////                e.printStackTrace()
////                runOnUiThread {
////                    // Handle error on the UI thread
////                }
////            }
////
////            override fun onResponse(call: Call, response: Response) {
////                if (response.isSuccessful) {
////                    val responseBody = response.body?.string()
////                    runOnUiThread {
////                        // Process the response on the UI thread
////                        println(responseBody) // or update the UI with the result
////                    }
////                } else {
////                    runOnUiThread {
////                        // Handle the error response (e.g., no item found)
////                    }
////                }
////            }
////        })
////    }
////
////    private fun onNextButtonClicked() {
////        binding.btnNext.setOnClickListener {
////            startActivity(Intent(this , OrderPlaceActivity::class.java))
////        }
////    }
////
////    private fun getAllCartProducts(){
////        viewModel.getAll().observe(this){
////            for (i in it){
////                cartProductList = it
////            }
////        }
////    }
////
////    //////////
////
////    override fun attachBaseContext(newBase: Context) {
////        val context: Context = LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase))
////        super.attachBaseContext(context)
////    }
////
////
////    ////////////
////
////
////    private fun onCartClicked() {
////        binding.llItemCart.setOnClickListener {
////            val bsCartProductsBinding = BsCartProductsBinding.inflate(LayoutInflater.from(this))
////
////            val bs = BottomSheetDialog(this)
////            bs.setContentView(bsCartProductsBinding.root)
////
////            bsCartProductsBinding.tvNumberOfProductCount.text = binding.tvNumberOfProductCount.text
////            bsCartProductsBinding.btnNext.setOnClickListener {
////                startActivity(Intent(this , OrderPlaceActivity::class.java))
////            }
////
////            adapterCartProducts = AdapterCartProducts()
////            bsCartProductsBinding.rvProductsItems.adapter = adapterCartProducts
////            adapterCartProducts.differ.submitList(cartProductList)
////
////            bs.show()
////
////        }
////
////    }
////
////    private fun getTotalItemCountInCart() {
////        viewModel.fetchTotalCartItemCount().observe(this){
////            if (it > 0){
////                binding.llCart.visibility = View.VISIBLE
////                binding.tvNumberOfProductCount.text = it.toString()
////            }
////            else{
////                binding.llCart.visibility = View.GONE
////            }
////        }
////    }
////
////    override fun showCartLayout(itemCount : Int) {
////        val previousCount = binding.tvNumberOfProductCount.text.toString().toInt()
////        val updatedCount = previousCount + itemCount
////
////        if (updatedCount > 0){
////            binding.llCart.visibility = View.VISIBLE
////            binding.tvNumberOfProductCount.text = updatedCount.toString()
////
////        }
////        else{
////            binding.llCart.visibility = View.GONE
////            binding.tvNumberOfProductCount.text = "0"
////        }
////    }
////
////    override fun savingCartItemCount(itemCount: Int) {
////        viewModel.fetchTotalCartItemCount().observe(this){
////            viewModel.savingCartItemCount(it + itemCount)
////        }
////    }
////
////    override fun hideCartLayout() {
////        binding.llCart.visibility = View.GONE
////        binding.tvNumberOfProductCount.text = "0"
////    }
////
////
////}
//
//
//package com.example.nutrikart.activity
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import com.example.nutrikart.CartListener
//import com.example.nutrikart.LocaleHelper
//import com.example.nutrikart.OrderPlaceActivity
//import com.example.nutrikart.adapters.AdapterCartProducts
//import com.example.nutrikart.databinding.ActivityUsersMainBinding
//import com.example.nutrikart.databinding.BsCartProductsBinding
//import com.example.nutrikart.roomdb.CartProductTable
//import com.example.nutrikart.viewmodels.UserViewModel
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import okhttp3.*
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.RequestBody.Companion.toRequestBody
//import org.json.JSONObject
//import java.io.IOException
//
//class UsersMainActivity : AppCompatActivity(), CartListener {
//    //println(responseBody)
//    private lateinit var binding: ActivityUsersMainBinding
//    private val viewModel: UserViewModel by viewModels()
//    private var cartProductList: List<CartProductTable> = emptyList()
//    private lateinit var adapterCartProducts: AdapterCartProducts
//    private val client = OkHttpClient()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//
//        super.onCreate(savedInstanceState)
//        binding = ActivityUsersMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        observeCartProducts()
//        observeTotalItemCountInCart()
//
//        setupCartClickListener()
//        setupNextButtonClickListener()
//    }
//
//    private fun observeCartProducts() {
//        viewModel.getAll().observe(this) { productList ->
//            cartProductList = productList
//            Log.d("srinath", "initi")
//            productList.forEach { item ->
//                fetchRecommendations(item.toString())
//            }
//        }
//    }
//
//    private fun fetchRecommendations(item: String) {
//        val json = JSONObject().apply { put("item", item) }
//        val mediaType = "application/json; charset=utf-8".toMediaType()
//        val body = json.toString().toRequestBody(mediaType)
//
//        val request = Request.Builder()
//            .url("http://192.168.10.217:5000/recommendations")
//            .post(body)
//            .build()
//
//        Log.d("srinath", "higgi")
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.body?.string()?.let { responseBody ->
//                    runOnUiThread {
//                        Log.d("srinath", "billu")
//                        println(responseBody)
//                        // Update UI if needed
//                    }
//                }
//            }
//        })
//    }
//
//    private fun setupNextButtonClickListener() {
//        binding.btnNext.setOnClickListener {
//            startActivity(Intent(this, OrderPlaceActivity::class.java))
//        }
//    }
//
//    private fun setupCartClickListener() {
//        binding.llItemCart.setOnClickListener {
//            val bsBinding = BsCartProductsBinding.inflate(LayoutInflater.from(this))
//            val bottomSheetDialog = BottomSheetDialog(this)
//            bottomSheetDialog.setContentView(bsBinding.root)
//
//            bsBinding.tvNumberOfProductCount.text = binding.tvNumberOfProductCount.text
//            bsBinding.btnNext.setOnClickListener {
//                startActivity(Intent(this, OrderPlaceActivity::class.java))
//            }
//
//            adapterCartProducts = AdapterCartProducts()
//            bsBinding.rvProductsItems.adapter = adapterCartProducts
//            adapterCartProducts.differ.submitList(cartProductList)
//
//            bottomSheetDialog.show()
//        }
//    }
//
//    private fun observeTotalItemCountInCart() {
//        viewModel.fetchTotalCartItemCount().observe(this) { itemCount ->
//            if (itemCount > 0) {
//                binding.llCart.visibility = View.VISIBLE
//                binding.tvNumberOfProductCount.text = itemCount.toString()
//            } else {
//                binding.llCart.visibility = View.GONE
//            }
//        }
//    }
//
//    override fun attachBaseContext(newBase: Context) {
//        val context: Context = LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase))
//        super.attachBaseContext(context)
//    }
//
//    override fun showCartLayout(itemCount: Int) {
//        val previousCount = binding.tvNumberOfProductCount.text.toString().toIntOrNull() ?: 0
//        val updatedCount = previousCount + itemCount
//
//        if (updatedCount > 0) {
//            binding.llCart.visibility = View.VISIBLE
//            binding.tvNumberOfProductCount.text = updatedCount.toString()
//        } else {
//            binding.llCart.visibility = View.GONE
//            binding.tvNumberOfProductCount.text = "0"
//        }
//    }
//
//    override fun savingCartItemCount(itemCount: Int) {
//        viewModel.fetchTotalCartItemCount().observe(this) {
//            viewModel.savingCartItemCount(it + itemCount)
//        }
//    }
//
//    override fun hideCartLayout() {
//        binding.llCart.visibility = View.GONE
//        binding.tvNumberOfProductCount.text = "0"
//    }
//}
package com.example.nutrikart.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrikart.CartListener
import com.example.nutrikart.LocaleHelper
import com.example.nutrikart.OrderPlaceActivity
import com.example.nutrikart.adapters.AdapterCartProducts
import com.example.nutrikart.databinding.ActivityUsersMainBinding
import com.example.nutrikart.databinding.BsCartProductsBinding
import com.example.nutrikart.roomdb.CartProductTable
import com.example.nutrikart.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import com.example.nutrikart.R
import org.json.JSONArray


class UsersMainActivity : AppCompatActivity(), CartListener {
    private lateinit var binding: ActivityUsersMainBinding
    private val viewModel: UserViewModel by viewModels()
    private var cartProductList: List<CartProductTable> = emptyList()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private val client = OkHttpClient()

    private val recommendedItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        observeCartProducts()
        observeTotalItemCountInCart()


        setupCartClickListener()
        setupNextButtonClickListener()
    }

    private fun observeCartProducts() {
        viewModel.getAll().observe(this) { productList ->
            cartProductList = productList
            Log.d("srinath", "Fetching recommendations")

            productList.forEach { item ->

                fetchRecommendations(item.productTitle ?: "Peas")
            }
        }
    }

    private fun fetchRecommendations(productTitle: String) {
        val json = JSONObject().apply { put("item", productTitle) }
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()  //server connection code
            .url("http://192.168.105.126:5001/recommendations") //change here ip only
            .post(body)
            .build()

        Log.d("srinath", "Sending request for: $productTitle")   //failed code to check in logcat
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            val recommendedTitles = mutableListOf<String>()
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {  //to get recommendation
                response.body?.string()?.let { responseBody ->
                    runOnUiThread {
                        Log.d("srinath", "Response: $responseBody")
                        // Update UI with the response if needed
                        try {

                            //val jsonArray = JSONArray(responseBody)
                            val jsonObject = JSONObject(responseBody)
                            val jsonArray = jsonObject.getJSONArray("recommendations")


                            for (i in 0 until jsonArray.length()) {
                                val productObject = jsonArray.getJSONObject(i)
                               // val title = productObject.getString("productTitle")
                                val title = productObject.getString("Name")

                                recommendedTitles.add(title)
                            }
                            recommendedTitles.forEach { title ->
                                if (!recommendedItems.contains(title)) {
                                    recommendedItems.add(title)
                                }
                            }


                            val recommendedArray: Array<String> = recommendedTitles.toTypedArray()
                            Log.d("srinath", "Recommended titles: ${recommendedArray.joinToString()}")

                        } catch (e: Exception) {
                            Log.e("srinath", "Failed to parse JSON", e)
                        }
                    }
                }
            }
        })
    }



    private fun setupNextButtonClickListener() {
        binding.btnNext.setOnClickListener {
            //startActivity(Intent(this, OrderPlaceActivity::class.java))
            //val recommendedItems = arrayListOf<String>("Chia Seeds", "Quinoa", "Vitamin D3") // replace with real list
            val intent = Intent(this, OrderPlaceActivity::class.java)
            val javaList: java.util.ArrayList<String?> = ArrayList(recommendedItems)
            intent.putStringArrayListExtra("recommended_items", javaList)
            startActivity(intent)
        }
    }

    private fun setupCartClickListener() {
        binding.llItemCart.setOnClickListener {
            val bsBinding = BsCartProductsBinding.inflate(LayoutInflater.from(this))
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(bsBinding.root)

            bsBinding.tvNumberOfProductCount.text = binding.tvNumberOfProductCount.text
            bsBinding.btnNext.setOnClickListener {
                startActivity(Intent(this, OrderPlaceActivity::class.java))
            }

            adapterCartProducts = AdapterCartProducts()
            bsBinding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

            bottomSheetDialog.show()
        }
    }

    private fun observeTotalItemCountInCart() {
        viewModel.fetchTotalCartItemCount().observe(this) { itemCount ->
            if (itemCount > 0) {
                binding.llCart.visibility = View.VISIBLE
                binding.tvNumberOfProductCount.text = itemCount.toString()
            } else {
                binding.llCart.visibility = View.GONE
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val context: Context = LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase))
        super.attachBaseContext(context)
    }

    override fun showCartLayout(itemCount: Int) {
        val previousCount = binding.tvNumberOfProductCount.text.toString().toIntOrNull() ?: 0
        val updatedCount = previousCount + itemCount

        if (updatedCount > 0) {
            binding.llCart.visibility = View.VISIBLE
            binding.tvNumberOfProductCount.text = updatedCount.toString()
        } else {
            binding.llCart.visibility = View.GONE
            binding.tvNumberOfProductCount.text = "0"
        }
    }

    override fun savingCartItemCount(itemCount: Int) {
        viewModel.fetchTotalCartItemCount().observe(this) {
            viewModel.savingCartItemCount(it + itemCount)
        }
    }

    override fun hideCartLayout() {
        binding.llCart.visibility = View.GONE
        binding.tvNumberOfProductCount.text = "0"
    }
}
