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
import com.example.ecworld.ui.data.models.Product
//import com.example.ecworld.ui.data.models.Variation
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.lifecycleScope
import com.example.ecworld.ui.data.models.Variation
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
        // Add your logic to save products here, for now we'll just print the data
        var productName=binding.edProductName.text.toString()
        var sizes=binding.Size.text.toString()
        val sizeList=sizes.split(",").map { it.trim() }
        sizeList.forEach{size->
            products.add(size)
        }
        println(productName)
        products.forEach { product ->
            println("Product: ${product}")

        }
        val productData = hashMapOf(
            "productName" to productName,
            "sizes" to sizeList
        )
        // Get a reference to the Firestore database
        val db = FirebaseFirestore.getInstance()

        // Add the product data to the "products" collection
        db.collection("products")
            .add(productData)
            .addOnSuccessListener { documentReference ->
                // Successfully added data to Firestore
                println("Product successfully added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Failed to add data to Firestore
                println("Error adding product: $e")
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}