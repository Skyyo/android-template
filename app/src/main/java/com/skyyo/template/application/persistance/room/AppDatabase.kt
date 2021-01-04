package com.skyyo.template.application.persistance.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skyyo.template.application.models.local.Product

@Database(
    entities = [
        Product::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}
