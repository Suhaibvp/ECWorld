package com.example.ecworld

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.room.Room

//import com.example.ecworld.ui.mainpage.MainFragment
import com.example.ecworld.ui.product.add.AddProductFragment
import com.example.ecworld.ui.theme.ECWorldTheme

class MainActivity : AppCompatActivity() {



    private lateinit var navController: NavController
    private var addproductll:LinearLayout?=null
    private var billingll:LinearLayout?=null
    private var inventoryll:LinearLayout?=null
    private var salesll:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optional: Force night mode, or set based on system preferences
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Set up Bottom Navigation View
        val bottomNavigationView = findViewById<LinearLayout>(R.id.bottomNavigationView) // Change this to your actual Bottom Navigation ID
        setupBottomNavigation(bottomNavigationView)

        // Optional: If you have an ActionBar, you can link it with the NavController
//        NavigationUI.setupActionBarWithNavController(this, navController)



        // Set up your Compose or fragment logic here
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, MainFragment())
//            .commit()
    }
    private fun setupBottomNavigation(bottomNav: LinearLayout) {
        bottomNav.findViewById<LinearLayout>(R.id.add_product_ll).setOnClickListener {
            navController.navigate(R.id.addProductFragment) // Replace with your actual fragment ID
        }

        bottomNav.findViewById<LinearLayout>(R.id.billing_ll).setOnClickListener {
            navController.navigate(R.id.billingFragment) // Replace with your actual fragment ID
        }

        bottomNav.findViewById<LinearLayout>(R.id.inventory_ll).setOnClickListener {
            navController.navigate(R.id.inventoryFragment) // Replace with your actual fragment ID
        }

        bottomNav.findViewById<LinearLayout>(R.id.sales_ll).setOnClickListener {
            navController.navigate(R.id.salesFragment) // Replace with your actual fragment ID
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

