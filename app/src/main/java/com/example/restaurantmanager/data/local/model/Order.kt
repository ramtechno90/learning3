package com.example.restaurantmanager.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customer_name: String,
    val items: String, // Stored as JSON
    val total: Double,
    val status: String,
    val created_at: Long
)
