package com.example.restaurantmanager.data.local.dao

import androidx.room.*
import com.example.restaurantmanager.data.local.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE status = :status")
    fun getOrdersByStatus(status: String): Flow<List<Order>>

    @Query("DELETE FROM orders WHERE status IN (:statuses)")
    suspend fun clearOrdersByStatus(statuses: List<String>)
}
