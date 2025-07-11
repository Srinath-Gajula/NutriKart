package com.example.nutrikart.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao

//interface CartProductDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCartProduct(products : CartProductTable)
//
//    @Update
//    suspend fun updateCartProduct(products: CartProductTable)
//
//    @Query("SELECT * FROM CartProductTable") //it should be here if we want to see the data in app inspection
//    fun getAllCartProducts() : LiveData<List<CartProductTable>>
//
//    @Query("DELETE FROM CartProductTable WHERE productId = :productId")
//    suspend fun deleteCartProduct(productId : String?)
//
//
//}

interface CartProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartProduct(products : CartProductTable) ///

    @Update
    fun updateCartProduct(products: CartProductTable)

    @Query("SELECT * FROM CartProductTable") //it should be here if we want to see the data in app inspection
    fun getAllCartProducts() : LiveData<List<CartProductTable>>

    @Query("DELETE FROM CartProductTable WHERE productId = :productId")
    fun deleteCartProduct(productId : String)  ///

    @Query("DELETE FROM CartProductTable")
    fun deleteCartProducts() ////


}