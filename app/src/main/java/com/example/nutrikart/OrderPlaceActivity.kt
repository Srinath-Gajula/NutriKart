package com.example.nutrikart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.nutrikart.activity.UsersMainActivity
import com.example.nutrikart.adapters.AdapterCartProducts
import com.example.nutrikart.databinding.ActivityOrderPlaceBinding
import com.example.nutrikart.databinding.AddressLayoutBinding
import com.example.nutrikart.models.Users
import com.example.nutrikart.roomdb.CartProductTable
import com.example.nutrikart.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class OrderPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderPlaceBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllCartProducts()
        backToUserMainActivity()
        onPlaceOrderClicked()

    }

    private fun onPlaceOrderClicked() {
        binding.btnNext.setOnClickListener {
            viewModel.getAddressStatus().observe(this){status ->
                if (status){
                    // payment work
                }
                else{
                    val addressLayoutBinding = AddressLayoutBinding.inflate(LayoutInflater.from(this))

                    val alertDialog = AlertDialog.Builder(this)
                        .setView(addressLayoutBinding.root)
                        .create()
                    alertDialog.show()

                    addressLayoutBinding.btnAdd.setOnClickListener {
                        saveAddress(alertDialog , addressLayoutBinding)
                    }

                }

            }

        }
    }

    private fun saveAddress(alertDialog: AlertDialog, addressLayoutBinding: AddressLayoutBinding){
        Utils.showDialog(this , "Processing")
        val userPinCode = addressLayoutBinding.etPinCode.text.toString()
        val userPhoneNumber = addressLayoutBinding.etPhoneNumber.text.toString()
        val userState = addressLayoutBinding.etState.text.toString()
        val userDistrict = addressLayoutBinding.etDistrict.text.toString()
        val userAddress = addressLayoutBinding.etDescriptiveAddress.text.toString()

        val address = "$userPinCode, $userDistrict($userState), $userAddress, $userPhoneNumber"



        lifecycleScope.launch {
            viewModel.saveUserAddress(address)
            viewModel.saveAddressStatus()
        }
        Utils.showDialog(this , "Saved..")
        alertDialog.dismiss()
        Utils.hideDialog()




    }

    private fun backToUserMainActivity() {
        binding.tbOrderFragment.setNavigationOnClickListener {
            startActivity(Intent(this , UsersMainActivity::class.java))
            finish()
        }
    }

    private fun getAllCartProducts() {
        viewModel.getAll().observe(this){cartProductList->

            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItems.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartProductList)

            var totalPrice = 0

            for (products in cartProductList){
                val price = products.productPrice?.substring(1)?.toInt()  //₹24
                val itemCount = products.productCount!!
                totalPrice += (price?.times(itemCount)!!)
            }

            binding.tvSubTotal.text = totalPrice.toString()

            if (totalPrice < 200){
                binding.tvDeliveryCharge.text = "₹25"
                totalPrice += 25
            }

            binding.tvGrandTotal.text = totalPrice.toString()

        }
    }
}