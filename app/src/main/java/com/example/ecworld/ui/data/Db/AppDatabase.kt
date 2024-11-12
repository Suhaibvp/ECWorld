// AppDatabase.kt
package com.example.ecworld.ui.data.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecworld.ui.data.dao.ProductDao
import com.example.ecworld.ui.data.dao.VariationDao
import com.example.ecworld.ui.data.Product
import com.example.ecworld.ui.data.Variation

@Database(entities = [Product::class, Variation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun variationDao(): VariationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ec_world_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
