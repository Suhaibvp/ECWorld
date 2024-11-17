package com.example.ecworld.ui.adapter

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
    private val onPlayClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.DVRViewHolder>() {

    class DVRViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.tvProductName)


        val playImageView: ImageView = itemView.findViewById(R.id.ivPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DVRViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return DVRViewHolder(view)
    }

    override fun onBindViewHolder(holder: DVRViewHolder, position: Int) {
        val item = products[position]
        holder.dateTextView.text = item.productName


        holder.playImageView.setOnClickListener { onPlayClick(item) }
    }

    override fun getItemCount(): Int = products.size
}
