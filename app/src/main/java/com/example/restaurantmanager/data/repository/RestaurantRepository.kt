package com.example.restaurantmanager.data.repository

import com.example.restaurantmanager.data.local.dao.CategoryDao
import com.example.restaurantmanager.data.local.dao.MenuItemDao
import com.example.restaurantmanager.data.local.dao.OrderDao
import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.data.local.model.Order
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val menuItemDao: MenuItemDao,
    private val categoryDao: CategoryDao,
    private val orderDao: OrderDao
) {
    // MenuItem operations
    fun getAllMenuItems(): Flow<List<MenuItem>> = menuItemDao.getAllMenuItems()
    fun getMenuItemById(id: Int): Flow<MenuItem> = menuItemDao.getMenuItemById(id)
    suspend fun insertMenuItem(menuItem: MenuItem) = menuItemDao.insert(menuItem)
    suspend fun updateMenuItem(menuItem: MenuItem) = menuItemDao.update(menuItem)
    suspend fun deleteMenuItem(menuItem: MenuItem) = menuItemDao.delete(menuItem)

    // Category operations
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    suspend fun insertCategory(category: Category) = categoryDao.insert(category)
    suspend fun updateCategory(category: Category) = categoryDao.update(category)
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    // Order operations
    fun getAllOrders(): Flow<List<Order>> = orderDao.getAllOrders()
    fun getOrdersByStatus(status: String): Flow<List<Order>> = orderDao.getOrdersByStatus(status)
    suspend fun insertOrder(order: Order): Long = orderDao.insert(order)
    suspend fun updateOrder(order: Order) = orderDao.update(order)
    suspend fun deleteOrder(order: Order) = orderDao.delete(order)
    suspend fun clearOrdersByStatus(statuses: List<String>) = orderDao.clearOrdersByStatus(statuses)
}
