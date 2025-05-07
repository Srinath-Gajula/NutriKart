package com.example.nutrikart.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutrikart.CartListener
import com.example.nutrikart.R
import com.example.nutrikart.Utils
import com.example.nutrikart.adapters.AdapterProduct
import com.example.nutrikart.databinding.FragmentSearchBinding
import com.example.nutrikart.databinding.ItemViewProductBinding
import com.example.nutrikart.models.Product
import com.example.nutrikart.roomdb.CartProductTable
import com.example.nutrikart.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    val viewModel : UserViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapterProduct: AdapterProduct
    private var cartListener : CartListener ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater)

        getAllTheProducts()
        searchProducts()
        backToHomeFragment()

        return binding.root
    }

//    private fun searchProducts() {
//        binding.searchEt.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTextChanged(
//                s: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                val query = s.toString().trim()
//                adapterProduct.filter.filter(query)
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                adapterProduct.getFilter().filter(query)
            }

            override fun afterTextChanged(s: Editable?) {

            }


        })
    }

    private fun backToHomeFragment() {
        binding.searchEt.setOnClickListener{
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun getAllTheProducts() {
        lifecycleScope.launch {
            viewModel.fetchAllTheProducts( ).collect{
                if (it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
                else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(
                    ::onAddButtonClicked,
                    ::onIncrementButtonClicked,
                    ::onDecrementButtonClicked
                )
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList = it as ArrayList<Product>
            }
        }
    }

//    private fun onAddButtonClicked(product: Product , productBinding: ItemViewProductBinding){
//        productBinding.tvAdd.visibility = View.GONE
//        productBinding.llProductCount.visibility = View.VISIBLE
//
//        //step 1
//        var itemCount = productBinding.tvProductCount.text.toString().toInt()
//        itemCount++
//
//        productBinding.tvProductCount.text = itemCount.toString()
//
//        cartListener?.showCartLayout(1)
//
//
//        //step 2 item count value storing in database
//        product.itemCount = itemCount
//        lifecycleScope.launch {
//            cartListener?.savingCartItemCount(1)
//            saveProductInRoomDb(product)
//            viewModel.updateItemCount(product , itemCount)
//        }
//
//    }

//    private fun onAddButtonClicked(product: Product , productBinding: ItemViewProductBinding) {
//        productBinding.tvAdd.visibility = View.GONE
//        productBinding.llProductCount.visibility = View.VISIBLE
//
//        //step 1
//        var itemCount = productBinding.tvProductCount.text.toString().toInt()
//        itemCount++
//
//        productBinding.tvProductCount.text = itemCount.toString()
//
//        cartListener?.showCartLayout(1)
//
//        //
//
//        cartListener?.savingCartItemCount(1)
//
//    }

    private fun onAddButtonClicked(product: Product , productBinding: ItemViewProductBinding) {
        productBinding.tvAdd.visibility = View.GONE
        productBinding.llProductCount.visibility = View.VISIBLE

        //step 1
        var itemCount = productBinding.tvProductCount.text.toString().toInt()
        itemCount++

        productBinding.tvProductCount.text = itemCount.toString()

        cartListener?.showCartLayout(1)


        //
        product.itemCount = itemCount
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product , itemCount)
        }


    }

//    private fun onAddButtonClicked(product: Product , productBinding: ItemViewProductBinding){
//        productBinding.tvAdd.visibility = View.GONE
//        productBinding.llProductCount.visibility = View.VISIBLE
//
//        //step 1
//        var itemCount = productBinding.tvProductCount.text.toString().toInt()
//        itemCount++
//
//        productBinding.tvProductCount.text = itemCount.toString()
//
//        cartListener?.showCartLayout(1)
//
//
//        //step 2
//        product.itemCount = itemCount
//        lifecycleScope.launch {
//            cartListener?.savingCartItemCount(1)
//            saveProductInRoomDb(product)
//            viewModel.updateItemCount(product , itemCount)
//        }
//
//    }

//    private fun onIncrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){
//
//        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
//        itemCountInc++
//        productBinding.tvProductCount.text = itemCountInc.toString()
//
//        cartListener?.showCartLayout(1)
//
//        //step 2
//        cartListener?.savingCartItemCount(1)
//
//    }

    private fun onIncrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){

        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
        itemCountInc++

        if (product.productStock!! + 1 > itemCountInc){
            productBinding.tvProductCount.text = itemCountInc.toString()

            cartListener?.showCartLayout(1)

            //step 2
            product.itemCount = itemCountInc
            lifecycleScope.launch {
                cartListener?.savingCartItemCount(1)
                saveProductInRoomDb(product)
                viewModel.updateItemCount(product , itemCountInc)
            }
        }
        else{
            Utils.showToast(requireContext() , "Can't add more item of this")
        }


    }

//    private fun onIncrementButtonClicked(product: Product, productBinding: ItemViewProductBinding){
//
//        var itemCountInc = productBinding.tvProductCount.text.toString().toInt()
//        itemCountInc++
//
//        if (product.productStock!! + 1 > itemCountInc){
//            productBinding.tvProductCount.text = itemCountInc.toString()
//
//            cartListener?.showCartLayout(1)
//
//            //step 2
//            product.itemCount = itemCountInc
//            lifecycleScope.launch {
//                cartListener?.savingCartItemCount(1)
//                saveProductInRoomDb(product)
//                viewModel.updateItemCount(product , itemCountInc)
//            }
//        }
//
//        else{
//            Utils.showToast(requireContext() , "Can't add more item of this")
//        }
//
//    }



//    fun onDecrementButtonClicked(product: Product , productBinding: ItemViewProductBinding){
//
//        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
//        itemCountDec--
//
//        if (itemCountDec > 0) {
//            productBinding.tvProductCount.text = itemCountDec.toString()
//        }
//        else{
//            productBinding.tvAdd.visibility = View.VISIBLE
//            productBinding.llProductCount.visibility = View.GONE
//            productBinding.tvProductCount.text = "0"
//        }
//
//        cartListener?.showCartLayout(-1)
//
//        //step 2
//        cartListener?.savingCartItemCount(-1)   //
//
//    }

    fun onDecrementButtonClicked(product: Product , productBinding: ItemViewProductBinding){

        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
        itemCountDec--

        product.itemCount = itemCountDec
        lifecycleScope.launch {
            cartListener?.savingCartItemCount(-1)
            saveProductInRoomDb(product)
            viewModel.updateItemCount(product , itemCountDec)
        }

        if (itemCountDec > 0) {
            productBinding.tvProductCount.text = itemCountDec.toString()
        }
        else{
            lifecycleScope.launch { viewModel.deleteCartProduct(product.productRandomId!!) }
            Log.d("VV" , product.productRandomId!!)
            productBinding.tvAdd.visibility = View.VISIBLE
            productBinding.llProductCount.visibility = View.GONE
            productBinding.tvProductCount.text = "0"
        }

        cartListener?.showCartLayout(-1)

        //step 2


    }

//    private fun onDecrementButtonClicked(product: Product , productBinding: ItemViewProductBinding){
//
//        var itemCountDec = productBinding.tvProductCount.text.toString().toInt()
//        itemCountDec--
//
//        product.itemCount = itemCountDec
//        lifecycleScope.launch {
//            cartListener?.savingCartItemCount(-1)
//            saveProductInRoomDb(product)
//            viewModel.updateItemCount(product , itemCountDec)
//        }
//
//        if (itemCountDec > 0) {
//            productBinding.tvProductCount.text = itemCountDec.toString()
//        }
//        else{
//            lifecycleScope.launch { viewModel.deleteCartProduct(product.productRandomId!!) }
//            Log.d("VV" , product.productRandomId!!)
//            productBinding.tvAdd.visibility = View.VISIBLE
//            productBinding.llProductCount.visibility = View.GONE
//            productBinding.tvProductCount.text = "0"
//        }
//
//        cartListener?.showCartLayout(-1)
//
//        //step 2
//
//
//    }


//    private fun saveProductInRoomDb(product: Product){
//        val cartProduct = CartProductTable(
//            productId = product.productRandomId!!,
//            productTitle = product.productTitle,
//            productQuantity = product.productQuantity.toString() + product.productUnit.toString(),
//            productPrice = "₹" + "${product.productPrice}",
//            productCount = product.itemCount,
//            productStock = product.productStock,
//            productImage = product.productImageUris?.get(0)!!,
//            productCategory = product.productCategory,
//            adminUid = product.adminUid,
//
//            )
//
//        lifecycleScope.launch { viewModel.insertCartProduct(cartProduct) }
//
//    }

    private fun saveProductInRoomDb(product: Product){
        val cartProduct = CartProductTable(
            productId = product.productRandomId!!,
            productTitle = product.productTitle,
            productQuantity = product.productQuantity.toString() + product.productUnit.toString(),
            productPrice = "₹" + "${product.productPrice}",
            productCount = product.itemCount,
            productStock = product.productStock,
            productImage = product.productImageUris?.get(0)!!,
            productCategory = product.productCategory,
            adminUid = product.adminUid,
            productType = product.productType,

            )

        lifecycleScope.launch { viewModel.insertCartProduct(cartProduct) }

    }

//    context: android.content.Context

    override fun onAttach(context : Context){
        super.onAttach(context)

        if (context is CartListener){
            cartListener = context
        }
        else{
            throw ClassCastException("Please implement cart listener")
        }

    }


}