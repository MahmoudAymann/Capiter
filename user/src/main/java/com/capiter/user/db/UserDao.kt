package com.capiter.user.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single


@Dao
interface UserDao {
    @Query("SELECT * FROM ProductItem")
    fun getAllProducts(): Observable<List<com.capiter.user.model.ProductItem>>

    @Insert
    fun insertAll(products: List<com.capiter.user.model.ProductItem>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: com.capiter.user.model.ProductItem): Completable

    @Delete
    fun deleteProduct(item: com.capiter.user.model.ProductItem): Single<Int>

    @Query("DELETE FROM ProductItem")
    fun deleteAll(): Completable

}