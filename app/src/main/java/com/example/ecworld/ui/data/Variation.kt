package com.example.ecworld.ui.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "variations",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Variation(
    @PrimaryKey(autoGenerate = true) val variationId: Long = 0,
    var productId: Long,  // Foreign key referencing Product
    val quality: String,
    val size: String,
    val price: Double

)