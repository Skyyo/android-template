package com.skyyo.template.application.persistance.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyyo.template.application.models.local.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Query("UPDATE products_table SET liked = :isLiked WHERE id = :productId")
    suspend fun setProductLiked(productId: Int, isLiked: Boolean)

    @Query("SELECT * from products_table")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * from products_table WHERE addedToCart = 1")
    fun getMyProducts(): Flow<List<Product>>

    @Query("SELECT COUNT(addedToCart) FROM products_table WHERE addedToCart = 1")
    fun getAddedToCartProductsCount(): Flow<Int>

    @Query("SELECT * FROM products_table WHERE id = :id")
    fun getProductById(id: Int): Flow<Product>
}
