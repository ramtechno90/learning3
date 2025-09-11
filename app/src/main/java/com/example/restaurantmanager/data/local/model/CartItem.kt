package com.example.restaurantmanager.data.local.model

data class CartItem(
    val menuItem: MenuItem,
    var dineInQuantity: Int,
    var takeawayQuantity: Int,
    var instructions: String
)
