package com.example.restaurantmanager.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val in_stock: Boolean = true,
    val takeaway_available: Boolean = true,
    val parcel_charge: Double = 0.0
)
