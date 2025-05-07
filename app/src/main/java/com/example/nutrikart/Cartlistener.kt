package com.example.nutrikart

interface CartListener {

    fun showCartLayout(itemCount : Int)

    fun savingCartItemCount(itemCount : Int)

    fun hideCartLayout()

}