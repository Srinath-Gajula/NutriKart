package com.example.nutrikart.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutrikart.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



//class RecommendationAdapter(private val items: List<String>) :
//    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_recommended_product, parent, false)
//        return RecommendationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
//        holder.tvName.text = items[position]
//        holder.ivImage.setImageResource(R.drawable.apple_img) // static image for now
//
//
//
//    }
//
//    override fun getItemCount() = items.size
//
//    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView = itemView.findViewById(R.id.tvProductTittle)
//        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage)
//    }
//}



//class RecommendationAdapter(private val items: List<String>) :
//    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {
//
//    private val databaseRef = FirebaseDatabase.getInstance().getReference("products")
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_recommended_product, parent, false)
//        return RecommendationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
//        val productName = items[position]
//        holder.tvName.text = productName
//
//        // Log productName to verify what is being queried
//        Log.d("FirebaseResponse", "Querying for product: $productName")
//
//        // Fetch product image from Firebase
//        databaseRef.orderByChild("productTitle").equalTo(productName)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        Log.d("FirebaseResponse", "Snapshot exists, checking products...")
//
//                        // Log all product titles in the snapshot for comparison
//                        snapshot.children.forEach { productSnapshot ->
//                            val productTitle = productSnapshot.child("productTitle").getValue(String::class.java)
//                            Log.d("FirebaseResponse", "Product Title from Firebase: $productTitle")
//                        }
//
//                        // Now, check for the matching title
//                        for (productSnapshot in snapshot.children) {
//                            val productTitle = productSnapshot.child("productTitle").getValue(String::class.java)
//                            if (productTitle != null && productTitle.contains(productName, ignoreCase = true)) {
//                                Log.d("FirebaseResponse", "Match found for product: $productTitle")
//
//                                val imageList = productSnapshot.child("productImageUris").children
//                                    .mapNotNull { it.getValue(String::class.java) }
//
//                                // Load the product image if images exist
//                                if (imageList.isNotEmpty()) {
//                                    Glide.with(holder.itemView)
//                                        .load(imageList[0]) // Load first image
//                                        .placeholder(R.drawable.default_product_img)
//                                        .into(holder.ivImage)
//                                } else {
//                                    holder.ivImage.setImageResource(R.drawable.default_product_img)
//                                }
//                            }
//                        }
//                    } else {
//                        Log.d("FirebaseResponse", "No data found for product: $productName")
//                        holder.ivImage.setImageResource(R.drawable.default_product_img)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("FirebaseResponse", "Failed to load product image", error.toException())
//                    holder.ivImage.setImageResource(R.drawable.default_product_img)
//                }
//            })
//    }
//
//
//    override fun getItemCount() = items.size
//
//    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView = itemView.findViewById(R.id.tvProductTittle)
//        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage)
//    }
//}


import com.google.firebase.database.*

//class RecommendationAdapter(private val items: List<String>) :
//    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {
//
//    private val databaseRef = FirebaseDatabase.getInstance().getReference("products")
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_recommended_product, parent, false)
//        return RecommendationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
//        val productName = items[position]
//        holder.tvName.text = productName
//
//        // Query product by title
//        databaseRef.orderByChild("productTitle").equalTo(productName)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.exists()) {
//                        for (productSnapshot in snapshot.children) {
//                            val productImageList = productSnapshot.child("productImageUris")
//                            val imageUris = mutableListOf<String>()
//
//                            for (imageSnap in productImageList.children) {
//                                imageSnap.getValue(String::class.java)?.let {
//                                    imageUris.add(it)
//                                }
//                            }
//
//                            if (imageUris.isNotEmpty()) {
//                                // Load first image using Glide
//                                Glide.with(holder.itemView)
//                                    .load(imageUris[0])
//                                    .placeholder(R.drawable.default_product_img)
//                                    .into(holder.ivImage)
//                            } else {
//                                holder.ivImage.setImageResource(R.drawable.default_product_img)
//                            }
//                        }
//                    } else {
//                        Log.d("FirebaseResponse", "No match for product: $productName")
//                        holder.ivImage.setImageResource(R.drawable.default_product_img)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.e("FirebaseResponse", "Error loading image for: $productName", error.toException())
//                    holder.ivImage.setImageResource(R.drawable.default_product_img)
//                }
//            })
//    }
//
//    override fun getItemCount() = items.size
//
//    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView = itemView.findViewById(R.id.tvProductTittle)
//        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage)
//    }
//}

class RecommendationAdapter(private val productNames: List<String>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val databaseRef = FirebaseDatabase.getInstance().getReference("Admins/AllProducts")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended_product, parent, false)
        return RecommendationViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val productName = productNames[position]
        holder.tvName.text = productName

        // Fetch the product by matching title
        databaseRef.orderByChild("productTitle").equalTo(productName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (productSnapshot in snapshot.children) {
                        // Load image
                        val imageUris = productSnapshot.child("productImageUris").children
                            .mapNotNull { it.getValue(String::class.java) }

                        if (imageUris.isNotEmpty()) {
                            Glide.with(holder.itemView)
                                .load(imageUris[0])
                                .placeholder(R.drawable.apple_img)   // Optional
                                .into(holder.ivImage)
                        }

                        // Load price and quantity
                        val price = productSnapshot.child("productPrice").getValue(Int::class.java)
                        val quantity = productSnapshot.child("productQuantity").getValue(Int::class.java)
                        val unit = productSnapshot.child("productUnit").getValue(String::class.java) ?: ""
//                        val description = productSnapshot.child("productDescription").getValue(String::class.java)
//                        holder.tvDescription.text = description ?: "No description available"

                        if (price != null) holder.tvPrice.text = "₹$price"
                        if (quantity != null) holder.tvQuantity.text = "$quantity $unit"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // handle error
                }
            })
    }


    override fun getItemCount() = productNames.size


    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProductTittle)
        val ivImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProductPrice)  //
        val tvQuantity: TextView = itemView.findViewById(R.id.tvProductQuantity)  //
//        val tvDescription: TextView = itemView.findViewById(R.id.tvProductDescription)
    }
}
