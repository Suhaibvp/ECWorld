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
import com.example.ecworld.ui.data.dao.ProductDao
import com.example.ecworld.ui.data.dao.VariationDao
import com.example.ecworld.databinding.FragmentAddProductBinding
import com.example.ecworld.ui.data.Product
import com.example.ecworld.ui.data.Variation
import com.example.ecworld.ui.data.Db.AppDatabase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class AddProductFragment : Fragment() {
    private val products = mutableListOf<Product>()
    private var _binding :FragmentAddProductBinding?=null
    private lateinit var productDao: ProductDao
    private lateinit var variationDao: VariationDao
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentAddProductBinding.inflate(inflater,container,false)
//        binding.btnSubmit.text="verify"
        val database = AppDatabase.getDatabase(requireContext())
        productDao = database.productDao()
        variationDao = database.variationDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addProductButton.setOnClickListener {
            val productListLayout=binding.productListLayout
            addProductUI(productListLayout)
        }
    }
    private fun addProductUI(productListLayout: LinearLayout) {
        val product = Product(name="product1")
        products.add(product)

        val productLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }

        val productNameEditText = EditText(requireContext()).apply {
            hint = "Enter Product Name"
        }
        productLayout.addView(productNameEditText)

        val addVariationButton = Button(requireContext()).apply {
            text = "Add Variation"
        }
        productLayout.addView(addVariationButton)

        // Set up variations layout
        val variationsLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }
        productLayout.addView(variationsLayout)

        addVariationButton.setOnClickListener {
            addVariationUI(product, variationsLayout)
        }
        // Add Save Button logic
        binding.saveProductsButton.setOnClickListener {
            saveProductsToDatabase()
        }

        productListLayout.addView(productLayout)
    }
    private fun saveProductsToDatabase() {
        lifecycleScope.launch {
            for (product in products) {
                val productId = productDao.insertProduct(product)

                for (variation in product.variations) {
                    variation.productId = productId // Link variation to the product
                    variationDao.insertVariation(variation)
                }
            }
        }
    }

    private fun addVariationUI(product: Product, variationsLayout: LinearLayout) {

        val variation = Variation(price=0.0, size = "", quality = "", productId = 0)
        product.variations.add(variation)

        val variationLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val qualityEditText = EditText(requireContext()).apply {
            hint = "Quality"
        }
        val sizeEditText = EditText(requireContext()).apply {
            hint = "Size"
        }
        val priceEditText = EditText(requireContext()).apply {
            hint = "Price"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        variationLayout.addView(qualityEditText)
        variationLayout.addView(sizeEditText)
        variationLayout.addView(priceEditText)
        variationsLayout.addView(variationLayout)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}