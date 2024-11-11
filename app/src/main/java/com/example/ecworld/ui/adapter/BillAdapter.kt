package com.example.ecworld.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.R
import com.example.ecworld.ui.data.BillItem

class BillAdapter(private val items: List<BillItem>) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemSize: TextView = view.findViewById(R.id.itemSize)
        val itemQty: TextView = view.findViewById(R.id.itemQty)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val totalPrice:TextView=view.findViewById(R.id.totalPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val currentItem = items[position]
        holder.itemName.text = currentItem.name
        holder.itemSize.text = currentItem.size
        holder.itemQty.text = currentItem.quantity.toString()
        holder.itemPrice.text = currentItem.price.toString()
        val total = currentItem.quantity * currentItem.price
        holder.totalPrice.text = "%.2f".format(total)  // Formats to 2 decimal places


    }

    override fun getItemCount(): Int = items.size
}
