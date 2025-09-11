package com.example.restaurantmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.restaurantmanager.data.local.converter.CartItemConverter
import com.example.restaurantmanager.data.local.dao.CategoryDao
import com.example.restaurantmanager.data.local.dao.MenuItemDao
import com.example.restaurantmanager.data.local.dao.OrderDao
import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.data.local.model.Order

@Database(
    entities = [MenuItem::class, Category::class, Order::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CartItemConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
}
