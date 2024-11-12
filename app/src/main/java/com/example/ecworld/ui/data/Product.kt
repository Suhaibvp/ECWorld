package com.example.ecworld.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val name: String,
    val variations: MutableList<Variation> = mutableListOf()
)