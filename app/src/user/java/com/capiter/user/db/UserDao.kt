package com.capiter.user.db

import androidx.room.*
import com.capiter.user.ui.product.ProductItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single


@Dao
interface UserDao {
    @Query("SELECT * FROM ProductItem")
    fun getAllProducts(): Observable<List<ProductItem>>

    @Insert
    fun insertAll(products: List<ProductItem>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: ProductItem): Completable

    @Delete
    fun deleteProduct(item: ProductItem): Single<Int>

    @Query("DELETE FROM ProductItem")
    fun deleteAll(): Completable

}