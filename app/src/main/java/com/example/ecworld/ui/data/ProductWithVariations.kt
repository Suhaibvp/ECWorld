// ProductWithVariations.kt
package com.example.ecworld.ui.data

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithVariations(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val variations: List<Variation>
)
