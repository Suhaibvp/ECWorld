package com.example.ecworld.ui.product.add

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.ecworld.databinding.FragmentAddProductBinding

//import com.example.ecworld.ui.data.models.Variation
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {
    private val products = mutableListOf<String>()
    private var _binding :FragmentAddProductBinding?=null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentAddProductBinding.inflate(inflater,container,false)
//        binding.btnSubmit.text="verify"


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveProductsButton.setOnClickListener {
            saveProducts()
        }

    }
    private fun saveProducts() {
        // Get the product name and size list
        var productName = binding.edProductName.text.toString()
        var sizes = binding.Size.text.toString()
        val sizeList = sizes.split(",").map { it.trim() }

        sizeList.forEach { size ->
            products.add(size)
        }

        println(productName)
        products.forEach { product ->
            println("Product: $product")
        }

        // Create the product data without productId first
        val productData = hashMapOf(
            "productName" to productName,
            "sizes" to sizeList
        )

        // Get a reference to the Firestore database
        val db = FirebaseFirestore.getInstance()
        val countersRef = db.collection("counters").document("productCounter") // Counter document

        // Fetch the current product counter from the "counters" collection
        countersRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Get the last used productId
                val lastProductId = document.getLong("lastProductId") ?: 0

                // Increment the productId by 1 for the new product
                val newProductId = lastProductId + 1

                // Add the productId to the product data
                productData["productId"] = newProductId

                // Add the new product with the unique productId to the "products" collection
                db.collection("products")
                    .add(productData)
                    .addOnSuccessListener { documentReference ->
                        // Successfully added product
                        println("Product successfully added with ID: ${documentReference.id} and productId: $newProductId")

                        // Update the product counter in the "counters" collection
                        countersRef.update("lastProductId", newProductId)
                    }
                    .addOnFailureListener { e ->
                        // Failed to add product
                        println("Error adding product: $e")
                    }
            } else {
                // Counter document doesn't exist, create it with the initial productId value
                val initialProductId = 1L // Starting from 1
                productData["productId"] = initialProductId

                // Add the product with productId to the "products" collection
                db.collection("products")
                    .add(productData)
                    .addOnSuccessListener { documentReference ->
                        // Successfully added product
                        println("Product successfully added with ID: ${documentReference.id} and productId: $initialProductId")

                        // Create the counter document with the initial lastProductId
                        countersRef.set(hashMapOf("lastProductId" to initialProductId))
                            .addOnSuccessListener {
                                println("Counter document created with initial productId value.")
                            }
                            .addOnFailureListener { e ->
                                // Failed to create the counter document
                                println("Error creating counter document: $e")
                            }
                    }
                    .addOnFailureListener { e ->
                        // Failed to add product
                        println("Error adding product: $e")
                    }
            }
        }.addOnFailureListener { e ->
            // Failed to fetch the counter document
            println("Error fetching counter document: $e")
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}