package com.example.restaurantmanager.data.local.dao

import androidx.room.*
import com.example.restaurantmanager.data.local.model.MenuItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(menuItem: MenuItem)

    @Update
    suspend fun update(menuItem: MenuItem)

    @Delete
    suspend fun delete(menuItem: MenuItem)

    @Query("SELECT * FROM menu_items")
    fun getAllMenuItems(): Flow<List<MenuItem>>

    @Query("SELECT * FROM menu_items WHERE id = :id")
    fun getMenuItemById(id: Int): Flow<MenuItem>
}
