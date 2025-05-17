package com.example.nutrikart

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutrikart.Constants.apiEndPoint
import com.example.nutrikart.activity.UsersMainActivity
import com.example.nutrikart.adapters.AdapterCartProducts
import com.example.nutrikart.adapters.RecommendationAdapter
import com.example.nutrikart.databinding.ActivityOrderPlaceBinding
import com.example.nutrikart.databinding.AddressLayoutBinding
import com.example.nutrikart.models.Orders
import com.example.nutrikart.models.Users
import com.example.nutrikart.roomdb.CartProductTable
import com.example.nutrikart.viewmodels.UserViewModel
import com.google.errorprone.annotations.Var
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.getValue
private var recommended: ArrayList<String>? = null
class OrderPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderPlaceBinding
    private val viewModel : UserViewModel by viewModels()
    private lateinit var adapterCartProducts: AdapterCartProducts
    private lateinit var b2BPGRequest : B2BPGRequest
    private var cartListener : CartListener ? = null
   // val recommendedItems = intent.getStringArrayListExtra("recommended_items")
    //val recommended = intent.getStringArrayListExtra("recommended_items")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recommended = intent.getStringArrayListExtra("recommended_items")

        getAllCartProducts()
        backToUserMainActivity()
        initializePhonePay()
        onPlaceOrderClicked()


        setupRecommendedItems()

    }



//    private fun setupRecommendedItems() {
//        val dummyList = listOf("Quinoa", "Chia Seeds", "Flax Oil") // Replace with real data later
//
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvRecommendations.layoutManager = layoutManager
//
//        val adapter = RecommendationAdapter(dummyList)
//        binding.rvRecommendations.adapter = adapter
//    }


    private fun setupRecommendedItems() {
        viewModel.getAll().observe(this) { cartProducts ->
            //val recommended = mutableListOf<String>()
            // val recommended = recommended

          //   Simple rule: If product name contains "Protein", recommend "Whey" etc.
//            for (product in cartProducts) {
//                if (product.productTitle?.contains("protein", ignoreCase = true) == true) {
//                    recommended.add("Whey Protein")
//                    recommended.add("Shaker Bottle")
//                } else if (product.productTitle?.contains("protein", ignoreCase = true) == true) {
//                    recommended.add("Almond Milk")
//                    recommended.add("Chia Seeds")
//                }
//            }
//            if (recommended != null) {
////            if (recommended.isEmpty()) {
////                recommended.addAll(listOf("Chia Seeds", "Quinoa", "Vitamin D3")) // fallback
////            }
//
//            val adapter = RecommendationAdapter(recommended.distinct())
//            binding.rvRecommendations.layoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            binding.rvRecommendations.adapter = adapter
//
//                }
            if (recommended != null) {
                // Safe to operate on recommended now

                // Create a copy of the list (to prevent concurrency issues)
                val recommendedItems = ArrayList(recommended)

                // If recommendedItems is empty, add fallback items
                if (recommendedItems.isEmpty()) {
                    recommendedItems.addAll(listOf("Chia Seeds", "Quinoa", "Vitamin D3"))
                }

                // Use distinct() for the adapter
                val adapter = RecommendationAdapter(recommendedItems.distinct())
                binding.rvRecommendations.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvRecommendations.adapter = adapter
            } else {
                // Handle the case where recommended is null
                val fallbackItems = listOf("Chia Seeds", "Quinoa", "Vitamin D3") // fallback
                val adapter = RecommendationAdapter(fallbackItems)
                binding.rvRecommendations.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvRecommendations.adapter = adapter
            }





        }
    }


    private fun initializePhonePay() {
        val data = JSONObject()
        PhonePe.init(this, PhonePeEnvironment.SANDBOX, Constants.MERCHANT_ID, "  ")   ///


        data.put("merchantId" , Constants.MERCHANT_ID)
        data.put("merchantTransactionId" , Constants.merchantTransactionId)
        data.put("amount" , 200)
        data.put("mobileNumber" , "3337779999")
        data.put("callbackUrl" , "https://webhook.site/callback-ur")

        val paymentInstrument = JSONObject()
        paymentInstrument.put("type" , "UPI_INTENT")
        paymentInstrument.put("targetApp" , "com.phonepe.simulator")  ///

        data.put("paymentInstrument" , paymentInstrument)

        val deviceContext = JSONObject()
        deviceContext.put("deviceOS", "ANDROID")
        data.put("deviceContext", deviceContext)

        val payloadBase64 = Base64.encodeToString(
            data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP
        )

        val checksum = sha256(payloadBase64 + Constants.apiEndPoint + Constants.SALT_KEY) + "###1";


         b2BPGRequest = B2BPGRequestBuilder()
            .setData(payloadBase64)
            .setChecksum(checksum)
            .setUrl(Constants.apiEndPoint)
            .build()

    }
    private fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold(""){ str, it -> str + "%02x".format(it)}
    }

    private fun onPlaceOrderClicked() {
        binding.btnNext.setOnClickListener {
            viewModel.getAddressStatus().observe(this){status ->
                if (status){
                    // payment work

                    saveOrder()





                    getPaymentView()



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

    val phonePayView = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            checkStatus()
        }
    }

    private fun checkStatus() {

        val xVerify = sha256("/pg/v1/status/${Constants.MERCHANT_ID}/${Constants.merchantTransactionId}${Constants.SALT_KEY}") + "###1"

        val headers = mapOf(
            "Content-Type" to "application/json",
            "X-VERIFY" to xVerify,
            "X-MERCHANT-ID" to Constants.MERCHANT_ID,
        )

        lifecycleScope.launch {
            viewModel.checkPayment(headers)
            viewModel.paymentStatus.collect { status ->
                if (status){
                    Utils.showToast(this@OrderPlaceActivity, "Payment done")

                    saveOrder() 
                    viewModel.deleteCartProducts()
                    viewModel.savingCartItemCount(0)
                    cartListener?.hideCartLayout()

                    // order save , delete products

                    Utils.hideDialog()
                    startActivity(Intent(this@OrderPlaceActivity , UsersMainActivity::class.java))
                    finish()
                }else{
                    Utils.showToast(this@OrderPlaceActivity, "Payment not done")
                }

            }
        }

    }


    private fun saveOrder() {
        viewModel.getAll().observe(this){cartProductsList ->
            if (cartProductsList.isNotEmpty()){
                viewModel.getUserAddress {address ->
                    val order = Orders(
                        orderId = Utils.getRandomId(), orderList = cartProductsList,
                        userAddress = address, orderStatus = 0, orderDate = Utils.getCurrentDate(),
                        orderingUserId = Utils.getCurrentUserId()
                    )
                    viewModel.saveOrderedProducts(order)
                }
                for (products in cartProductsList){
                    val count = products.productCount
                    val stock = products.productStock?.minus(count!!)
                    if (stock != null) {
                        viewModel.saveProductsAfterOrder(stock, products)
                    }



                    lifecycleScope.launch {
                        Utils.showToast(this@OrderPlaceActivity, "Order Saved")
                        viewModel.deleteCartProducts()
                        viewModel.savingCartItemCount(0)
                        cartListener?.hideCartLayout()
                        startActivity(Intent(this@OrderPlaceActivity , UsersMainActivity::class.java))
                        finish()
                    }



                }
            }
        }
    }

//    private fun getPaymentView() {
//        try {
//            PhonePe.getImplicitIntent(this, b2BPGRequest, "com.phonepe.simulator")
//                .let {
//                    phonePayView.launch(it)
//                }
//        }
//        catch (e : PhonePeInitException){
//            Utils.showToast(this , e.message.toString())
//        }
//
//    }

    private fun getPaymentView() {
        try {
            val intent = PhonePe.getImplicitIntent(this, b2BPGRequest, "com.phonepe.simulator")
            if (intent != null) {
                phonePayView.launch(intent)
            } else {
                Utils.showToast(this, "Intent is null. Check your PhonePe request setup.")
            }
        } catch (e: PhonePeInitException) {
            Utils.showToast(this, e.message.toString())
        }
    }


    private fun saveAddress(alertDialog: AlertDialog, addressLayoutBinding: AddressLayoutBinding){
        Utils.showDialog(this , "Processing...")
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
        Utils.showToast(this , "Saved...")
        alertDialog.dismiss()
        Utils.hideDialog()


        getPaymentView() //////

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