package com.example.restaurantmanager.data.local.converter

import androidx.room.TypeConverter
import com.example.restaurantmanager.data.local.model.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartItemConverter {
    @TypeConverter
    fun fromCartItemList(cartItems: List<CartItem>): String {
        val gson = Gson()
        return gson.toJson(cartItems)
    }

    @TypeConverter
    fun toCartItemList(cartItemsString: String): List<CartItem> {
        val gson = Gson()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(cartItemsString, type)
    }
}
