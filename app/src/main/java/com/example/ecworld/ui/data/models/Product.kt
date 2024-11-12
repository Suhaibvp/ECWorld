package com.example.ecworld.ui.data.models

data class Product(
    val name: String,
    val variations: MutableList<Variation> = mutableListOf()
)

data class Variation(
    var quality: String = "",
    var size: String = "",
    var price: Double = 0.0,
    var productId :Long=0
)
