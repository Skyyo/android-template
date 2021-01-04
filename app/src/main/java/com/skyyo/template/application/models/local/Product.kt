package com.skyyo.template.application.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey
    val id: Long,
    val liked: Boolean,
    val addedToCart: Boolean
)
