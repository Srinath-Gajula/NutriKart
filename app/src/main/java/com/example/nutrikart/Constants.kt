package com.example.nutrikart

import com.phonepe.intent.sdk.api.MerchantAPI

object Constants {

//    val MERCHANT_ID = "PGTESTPAYUAT"
    val MERCHANT_ID = "TEST-M22KOSOT3FVEX_25050"
//    val SALT_KEY = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"
    val SALT_KEY = "ZTEwNmY0NGMtYzkxMS00ZjEzLWI1MjUtMTZhODU5MDVmOTQ1"
    var apiEndPoint = "/pg/v1/pay"
    val merchantTransactionId = "txnId"


    val allProductsCategory = arrayOf(
        "Fresh Vegetables",
        "Fresh Fruits",
        "Milk & Dairy",
        "Coconut Water",
        "Pulses & Grains",
        "Atta & Rice",
        "Dry Fruits & Nuts",
        "Snacks & Namkeen",
        "Masala & Spices",
        "Biscuits & Bakery",
        "Eggs",
        "Tea, Coffee & Beverages",
        "Pickles & Sauces",
        "Ice Creams & Desserts",
        "Salt, Sugar & Oil",
        "Pooja Items",
        "Fresh Flowers",
        "Personal Care",
        "Pet Care",
        )

//    val allProductsCategory = arrayOf(
//        getString(R.string.cat_fresh_vegetables),
//        getString(R.string.cat_fresh_fruits),
//        getString(R.string.cat_milk_dairy),
//        getString(R.string.cat_coconut_water),
//        getString(R.string.cat_dry_fruits),
//        getString(R.string.cat_snacks_namkeen),
//        getString(R.string.cat_pulses_grains),
//        getString(R.string.cat_atta_rice),
//        getString(R.string.cat_masala_spices),
//        getString(R.string.cat_biscuits_bakery),
//        getString(R.string.cat_eggs),
//        getString(R.string.cat_beverages),
//        getString(R.string.cat_pickles_sauces),
//        getString(R.string.cat_desserts),
//        getString(R.string.cat_salt_sugar_oil),
//        getString(R.string.cat_pooja_items),
//        getString(R.string.cat_fresh_flowers),
//        getString(R.string.cat_personal_care),
//        getString(R.string.cat_pet_care),
//    )


    val allProductsCategoryIcon = arrayOf(

        R.drawable.vegetables,
        R.drawable.fruits,
        R.drawable.milk,
        R.drawable.coconut,
        R.drawable.pulsesgrains,
        R.drawable.atta_rice,
        R.drawable.dryfruit,
        R.drawable.snacks,
        R.drawable.bg_category,
        R.drawable.biscuit,
        R.drawable.eggs,
        R.drawable.bg_category,
        R.drawable.pickles,
        R.drawable.bg_category,
        R.drawable.bg_category,
        R.drawable.pooja,
        R.drawable.flowers,
        R.drawable.bg_category,
        R.drawable.bg_category,

    )


}