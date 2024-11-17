package com.example.ecworld.ui.product.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.databinding.FragmentInventoryBinding
import com.example.ecworld.ui.adapter.ProductAdapter
import com.example.ecworld.ui.data.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class inventoryFragment :Fragment(){
    private val products = mutableListOf<Product>()
    private var _binding:FragmentInventoryBinding?=null
    private val binding get() = _binding!!
    private val firestore=FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentInventoryBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

fetchProducts()
        // Hardcoded DVR items
//        val products = listOf(
//            Product("Bed"),
//            Product("Hostel Bed"),
//            Product("Pillow")
//        )


    }
    private fun fetchProducts(){
        val recyclerView: RecyclerView = binding.rvDVRFiles
        firestore.collection("products").get().addOnSuccessListener {
            querySnapshot->
            for( document in querySnapshot){
                val product=document.toObject(Product::class.java)
                products.add(product)

        }
            Log.d("ProductsActivity", "Products: $products")
            val adapter = ProductAdapter(products) { item ->

                // Add logic for play button if needed
            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext())
        }.addOnFailureListener { exception ->
            Log.e("ProductsActivity", "Error fetching products", exception)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}