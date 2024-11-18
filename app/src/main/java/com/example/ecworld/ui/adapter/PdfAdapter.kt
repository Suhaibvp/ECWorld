package com.example.ecworld.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.R
import com.example.ecworld.ui.data.models.PdfFile

class PdfAdapter(
    private val pdfFiles: List<PdfFile>,
    private val onItemClick: (PdfFile) -> Unit
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.tvFileName)

        init {
            itemView.setOnClickListener {
                onItemClick(pdfFiles[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.fileName.text = pdfFiles[position].name
    }

    override fun getItemCount(): Int = pdfFiles.size
}
