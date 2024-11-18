package com.example.ecworld.ui.data.models

data class Product(
    var productId: Long = 0,
    val productName: String="",
    val sizes: List<String> = emptyList()

)
