package com.capiter.user.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.capiter.user.ui.product.ProductItem

/**
 * Main database description.
 */
@Database(
    entities = [ProductItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao
}
