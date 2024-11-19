package com.example.ecworld.ui.product.sales

import android.app.AlertDialog
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecworld.R
import com.example.ecworld.databinding.FragmentBillingBinding
import com.example.ecworld.ui.adapter.BillAdapter
import com.example.ecworld.ui.data.models.BillItem
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class billingFragment :Fragment(){
    private lateinit var billRecyclerView: RecyclerView
    private lateinit var addButton: Button
    private val items = mutableListOf<BillItem>()
    private lateinit var adapter: BillAdapter
    private var _binding:FragmentBillingBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentBillingBinding.inflate(inflater,container,false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        billRecyclerView = view.findViewById(R.id.billRecyclerView)
        addButton = view.findViewById(R.id.addButton)

        adapter = BillAdapter(items)
        billRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        billRecyclerView.adapter = adapter


        val itemSpinner: Spinner = binding.appCompatSpinnerItems
        val sizeSpinner: Spinner = binding.appCompatSpinnerSizes

        fetchProductNames(
            onSuccess = { productNames ->
                if (isAdded) { // Ensure the fragment is attached
                    val itemAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productNames)
                    itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    itemSpinner.adapter = itemAdapter
                } else {
                    Log.d("BillingFragment", "Fragment is not attached. Skipping setting item spinner.")
                }
            },
            onError = { exception ->
                Log.e("FirebaseError", "Error fetching product names: ${exception.message}")
            }
        )
        // Add listener to fetch sizes when an item is selected
        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = itemSpinner.selectedItem.toString()
                fetchSizesForItem(selectedItem,
                    onSuccess = { sizes ->
                        val sizeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sizes)
                        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        sizeSpinner.adapter = sizeAdapter
                    },
                    onError = { exception ->
                        Log.e("FirebaseError", "Error fetching sizes: ${exception.message}")
                    }
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when no item is selected if needed
            }
        }

        addButton.setOnClickListener {
            // Collect data from input fields and add to items list
            val itemName =
                view.findViewById<Spinner>(R.id.appCompatSpinnerItems).selectedItem.toString()
            val itemSize =
                view.findViewById<Spinner>(R.id.appCompatSpinnerSizes).selectedItem.toString()
            val quantity =
                view.findViewById<EditText>(R.id.edQty).text.toString().toIntOrNull() ?: 0
            val price =
                view.findViewById<EditText>(R.id.edPrice).text.toString().toDoubleOrNull() ?: 0.0

            items.add(BillItem(itemName, itemSize, quantity, price))
            adapter.notifyDataSetChanged()
        }
//        binding.btnSaveBill.setOnClickListener{
//            generatePDF()
//        }
        binding.btnSaveBill.setOnClickListener {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_company_info, null)
            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            val etCompanyName = dialogView.findViewById<EditText>(R.id.etCompanyName)
            val etMobileNumber = dialogView.findViewById<EditText>(R.id.etMobileNumber)
            val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

            btnSubmit.setOnClickListener {
                val companyName = etCompanyName.text.toString()
                val mobileNumber = etMobileNumber.text.toString()

                // Generate PDF with this information
                generatePDF(companyName, mobileNumber)

                dialog.dismiss()
            }

            dialog.show()
        }

    }
    fun fetchSizesForItem(itemName: String, onSuccess: (List<String>) -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .whereEqualTo("productName", itemName)
            .get()
            .addOnSuccessListener { result ->
                val sizes = result.documents.firstOrNull()?.get("sizes") as? List<String> ?: emptyList()
                onSuccess(sizes) // Pass the list of sizes to the success callback
            }
            .addOnFailureListener { exception ->
                onError(exception) // Pass the exception to the error callback
            }
    }

    fun fetchProductNames(onSuccess: (List<String>) -> Unit, onError: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productNames = result.map { document ->
                    document.getString("productName") ?: "Unknown"
                }
                onSuccess(productNames) // Pass the list of names to the success callback
            }
            .addOnFailureListener { exception ->
                onError(exception) // Pass the exception to the error callback
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun generatePDF(clientName: String, mobileNumber: String){
        val pdfDocument=PdfDocument()
        val pageInfo=PdfDocument.PageInfo.Builder(595,842,1).create()
        val page=pdfDocument.startPage(pageInfo)
        val canvas=page.canvas
        val paint=Paint()
        paint.textSize=11f
        paint.isFakeBoldText=false
        val companyName="RAREERAM FURNITURE"
        val Address1="Chemmaniyode, Melattur, Malappuram"
        val Address2="Kerala, India, 679325"
//        val clientName="Test Client 1"
        val clientPlace=""
        val clientMobNu="9894493002"
        val clientMessage="Thanks For Purchase"
        canvas.drawText(companyName,40f,40f,paint)
        canvas.drawText(Address1,40f,60f,paint)
        canvas.drawText(Address2,40f,80f,paint)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        canvas.drawText("Date: $currentDate",450f,80f,paint)
        val linePaint1=Paint()
        val linePaint2=Paint()
        linePaint2.strokeWidth=1f
        linePaint2.color=Color.GRAY
        linePaint1.strokeWidth=2f
        linePaint1.color = Color.GRAY
        canvas.drawLine(40f, 100f, 555f, 100f, linePaint1)
        paint.textSize=16f
        paint.isFakeBoldText=true
        canvas.drawText(companyName,40f,140f,paint)
        paint.textSize=11f
        paint.isFakeBoldText=false
        canvas.drawText(clientMessage,40f,160f,paint)
        canvas.drawLine(40f,200f,555f,200f,linePaint2)
        paint.textSize=13f
        paint.isFakeBoldText=true
        canvas.drawText("BILL TO",40f,220f,paint)
        paint.textSize=11f
        paint.isFakeBoldText=false
        canvas.drawText(clientName,40f,235f,paint)
        canvas.drawText(clientPlace,40f,250f,paint)
        canvas.drawText(mobileNumber,40f,265f,paint)
        canvas.drawLine(40f,280f,555f,280f,linePaint2)
        paint.textSize=13f
        paint.isFakeBoldText=true
        canvas.drawText("Item",40f,300f,paint)
        canvas.drawText("QTY",300f,300f,paint)
        canvas.drawText("Price",400f,300f,paint)
        canvas.drawText("Total",500f,300f,paint)
        canvas.drawLine(40f,320f,555f,320f,linePaint2)
        var yPosition = 340f
        var total :Double=0.0
        items.forEach { item ->
            var linePosition=yPosition+20
            canvas.drawText(item.name, 40f, yPosition, paint)
            canvas.drawText(item.size, 140f, yPosition, paint)  // Display size
            canvas.drawText(item.quantity.toString(), 300f, yPosition, paint)
            canvas.drawText("%.2f".format(item.price), 400f, yPosition, paint)
            canvas.drawText("%.2f".format(item.quantity * item.price), 500f, yPosition, paint)
            canvas.drawLine(40f,linePosition,555f,linePosition,linePaint2)
            total+=item.quantity * item.price
            yPosition += 40f
        }
        canvas.drawText("Subtotal",40f,yPosition+20,paint)
        canvas.drawText("\$ $total",500f,yPosition+20,paint)
        canvas.drawText("Tax",40f,yPosition+40,paint)
        canvas.drawText("\$ 000.00",500f,yPosition+40,paint)
        canvas.drawLine(40f,yPosition+60,555f,yPosition+60,linePaint2)
        canvas.drawText("Total Duo",40f,yPosition+80,paint)
        canvas.drawText("\$ $total",500f,yPosition+80,paint)
        pdfDocument.finishPage(page)

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val ecworldDir = File(downloadsDir, "ecworld")
        if (!ecworldDir.exists()) {
            ecworldDir.mkdirs()  // Create directory if it doesn't exist
        }
        val datetime=LocalDateTime.now()
        val formater=DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        val formattedTime=datetime.format(formater)

        val file = File(ecworldDir, "$clientName-$formattedTime.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF Saved at ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
    }
}