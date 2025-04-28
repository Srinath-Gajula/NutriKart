package com.example.nutrikart.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nutrikart.Utils
import com.example.nutrikart.models.Product
import com.example.nutrikart.roomdb.CartProductDao
import com.example.nutrikart.roomdb.CartProductTable
import com.example.nutrikart.roomdb.CartProductsDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import androidx.core.content.edit
import com.example.nutrikart.models.Users

class UserViewModel(application: Application) : AndroidViewModel(application) {


    val sharedPreferences : SharedPreferences = application.getSharedPreferences("My_Pref" , MODE_PRIVATE)
    val cartProductDao : CartProductDao = CartProductsDatabase.getDatabaseInstance(application).cartProductsDao()

    //Room DB
    suspend fun insertCartProduct(products: CartProductTable){
        cartProductDao.insertCartProduct(products)
    }

    fun getAll(): LiveData<List<CartProductTable>>{
        return cartProductDao.getAllCartProducts()
    }

    suspend fun updateCartProduct(products: CartProductTable){
        cartProductDao.updateCartProduct(products)
    }

    suspend fun deleteCartProduct(productId : String){
        cartProductDao.deleteCartProduct(productId)
    }

    //Firebase call
    fun fetchAllTheProducts(): Flow<List<Product>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children){

                    val prod = product.getValue(Product::class.java)

                    products.add(prod!!)

                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        db.addValueEventListener(eventListener)

        awaitClose{
            db.removeEventListener(eventListener)
        }
    }

    fun saveUserAddress(address : String){
        FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserId()).child("userAddress").setValue(address)
    }

//    fun getCategoryProduct(category : String) : Flow<List<Product>> = callbackFlow{
//        val db = FirebaseDatabase.getInstance().getReference("Admins").child("productCategory/${category}")
//
//        val eventListener = object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val products = ArrayList<Product>()
//                for (product in snapshot.children){
//                    val prod = product.getValue(Product::class.java)
//
//                    products.add(prod!!)
//
//                }
//                trySend(products)
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        }
//
//        db.addValueEventListener(eventListener)
//
//        awaitClose{
//            db.removeEventListener(eventListener)
//        }
//
//    }



    fun getUserAddress(callback : (String?) -> Unit){
        val db = FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserId()).child("userAddress")

        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val address = snapshot.getValue(String::class.java)
                    callback(address)
                }else{
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError){
                callback(null)
            }

        })

    }

    fun saveAddress(address : String){
        FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(Utils.getCurrentUserId()).child("userAddress").setValue(address)
    }

    fun logOutUser(){
        FirebaseAuth.getInstance().signOut()
    }

    fun getCategoryProduct(category : String) : Flow<List<Product>> = callbackFlow{
        val db = FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${category}")

        val eventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children){
                    val prod = product.getValue(Product::class.java)
                    products.add(prod!!)

                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        db.addValueEventListener(eventListener)

        awaitClose{
            db.removeEventListener(eventListener)
        }

    }

    fun updateItemCount(product: Product , itemCount: Int){
        FirebaseDatabase.getInstance().getReference("Admins").child("AllProducts/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admins").child("ProductCategory/${product.productCategory}/${product.productRandomId}").child("itemCount").setValue(itemCount)
        FirebaseDatabase.getInstance().getReference("Admins").child("ProductType/${product.productType}/${product.productRandomId}").child("itemCount").setValue(itemCount)
    }

    //sharePreferences
    fun savingCartItemCount(itemCount: Int) {
        sharedPreferences.edit().putInt("itemCount" , itemCount).apply()

    }

    fun fetchTotalCartItemCount() : MutableLiveData<Int>{
            val totalItemCount = MutableLiveData<Int>()
            totalItemCount.value = sharedPreferences.getInt("itemCount" , 0)
            return totalItemCount
    }

    fun saveAddressStatus() {
        sharedPreferences.edit().putBoolean("addressStatus" , true).apply()
    }

    fun getAddressStatus() : MutableLiveData<Boolean>{
        val status = MutableLiveData<Boolean>()
        status.value = sharedPreferences.getBoolean("addressStatus" , false)
        return status
    }
}