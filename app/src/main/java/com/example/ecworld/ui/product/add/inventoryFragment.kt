package com.example.ecworld.ui.product.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.R
import com.example.ecworld.databinding.FragmentInventoryBinding
import com.example.ecworld.ui.adapter.ProductAdapter
import com.example.ecworld.ui.data.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class InventoryFragment : Fragment() {
    private val products = mutableListOf<Product>()
    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            products,
            onEditClick = { product ->
                Log.d("LogEditMenu", "onEditClick triggered for: ${product.productName}")
                showEditPopup(product)
            }
        )

        binding.rvDVRFiles.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchProducts() {
        firestore.collection("products").get()
            .addOnSuccessListener { querySnapshot ->
                products.clear()
                for (document in querySnapshot) {
                    val product = document.toObject(Product::class.java)
                    product.productId = document.getLong("productId") ?: 0L
                    products.add(product)
                }

                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("InventoryFragment", "Error fetching products", exception)
            }
    }

    private fun showEditPopup(product: Product) {
        Log.d("LogEditMenu","edit popup triggered2")
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_product, null)
        val productNameEditText: EditText = dialogView.findViewById(R.id.etProductName)
        val productSizesEditText: EditText = dialogView.findViewById(R.id.etProductSizes)

        // Set current values
        productNameEditText.setText(product.productName)
        productSizesEditText.setText(product.sizes.joinToString(",")) // Convert list to comma-separated string

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Product")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedName = productNameEditText.text.toString()
                val updatedSizes = productSizesEditText.text.toString().split(",").map { it.trim() }

                if (updatedName.isNotEmpty() && updatedSizes.isNotEmpty()) {
                    val updatedProduct = product.copy(productName = updatedName, sizes = updatedSizes)
                    updateProductInFirestore(updatedProduct)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateProductInFirestore(product: Product) {
        product.productId?.let { productId ->
            firestore.collection("products").document(productId.toString())
                .set(product)
                .addOnSuccessListener {
                    Log.d("InventoryFragment", "Product updated successfully")
                    val index = products.indexOfFirst { it.productId == productId }
                    if (index != -1) {
                        products[index] = product
                        productAdapter.notifyItemChanged(index)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("InventoryFragment", "Error updating product", exception)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
