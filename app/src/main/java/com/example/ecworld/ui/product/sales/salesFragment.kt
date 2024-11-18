package com.example.ecworld.ui.product.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecworld.databinding.FragmentSalesBinding


import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecworld.ui.adapter.PdfAdapter
import com.example.ecworld.ui.data.models.PdfFile

import java.io.File

class salesFragment : Fragment() {

    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val pdfFiles = getPdfFilesFromFolder()
        val adapter = PdfAdapter(pdfFiles) { pdfFile ->
            // Handle PDF item click
            // For example, open the PDF file
            openPdf(pdfFile.path)
        }
        binding.pdfRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.pdfRecyclerView.adapter = adapter
    }

    private fun getPdfFilesFromFolder(): List<PdfFile> {
        val pdfList = mutableListOf<PdfFile>()
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        val ecworldDir = File(folder, "ecworld")

        if (ecworldDir.exists()) {
            ecworldDir.listFiles()?.forEach { file ->
                if (file.extension == "pdf") {
                    pdfList.add(PdfFile(file.name, file.absolutePath))
                }
            }
        }

        return pdfList
    }

    private fun openPdf(filePath: String) {
        // Open the PDF file using an external PDF viewer
        // (Implementation depends on your app's requirements)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
