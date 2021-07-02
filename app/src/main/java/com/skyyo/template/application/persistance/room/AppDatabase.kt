package com.skyyo.template.application.persistance.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skyyo.template.application.models.local.Product

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        Product::class
    ],
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
        // when updating just add AutoMigration (from = 2, to = 3) and change the DB version
    ],

)

abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}
