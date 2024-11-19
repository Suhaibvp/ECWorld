package com.example.ecworld.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.R
import com.example.ecworld.ui.data.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductAdapter(
    private val products: List<Product>,
    private val onEditClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        val dltImageView: ImageView = itemView.findViewById(R.id.ivDelete)
        val productIdViewHolder:TextView=itemView.findViewById(R.id.tvProductId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTextView.text = product.productName
        holder.productIdViewHolder.text=product.productId.toString()

        Log.d("ProductAdapter", "Binding product: ${product.productName}")
        holder.dltImageView.setOnClickListener {
            Log.d("LogEditMenu", "edit popup triggered for product: ${product.productName}")
            deleteItem(product, position)
//            showEditPopup(product)
//            onEditClick(product)
        }
    }
    private fun deleteItem(product: Product, position: Int) {
        val firestore = FirebaseFirestore.getInstance()

        // Query Firestore to find the document with matching productId
        firestore.collection("products")
            .whereEqualTo("productId", product.productId) // Match the artificial productId
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0] // Get the first matching document
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("ProductAdapter", "Product deleted successfully from Firestore")

                            // Remove item from the local list
                            (products as MutableList).removeAt(position)

                            // Notify adapter to refresh the UI
                            notifyItemRemoved(position)
                        }
                        .addOnFailureListener { exception ->
                            Log.e("ProductAdapter", "Failed to delete product", exception)
                        }
                } else {
                    Log.e("ProductAdapter", "No matching product found in Firestore")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ProductAdapter", "Error querying Firestore", exception)
            }
    }



    override fun getItemCount(): Int = products.size
}

