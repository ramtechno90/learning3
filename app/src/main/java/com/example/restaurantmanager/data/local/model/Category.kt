package com.example.restaurantmanager.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories", indices = [Index(value = ["name"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
