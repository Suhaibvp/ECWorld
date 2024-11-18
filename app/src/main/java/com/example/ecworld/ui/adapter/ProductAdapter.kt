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

class ProductAdapter(
    private val products: List<Product>,
    private val onEditClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.tvProductName)
        val editImageView: ImageView = itemView.findViewById(R.id.ivEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTextView.text = product.productName
        holder.editImageView.setOnClickListener {
            Log.d("LogEditMenu","edit popup triggered")
            onEditClick(product)
        }
    }

    override fun getItemCount(): Int = products.size
}

