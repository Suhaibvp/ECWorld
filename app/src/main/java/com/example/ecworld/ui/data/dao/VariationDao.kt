// VariationDao.kt
package com.example.ecworld.ui.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecworld.ui.data.Variation

@Dao
interface VariationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariation(variation: Variation): Long

    @Query("SELECT * FROM variations WHERE productId = :productId")
    suspend fun getVariationsForProduct(productId: Long): List<Variation>
}
