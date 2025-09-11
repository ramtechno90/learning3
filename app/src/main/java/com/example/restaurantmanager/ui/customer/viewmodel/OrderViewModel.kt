package com.example.restaurantmanager.ui.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.data.local.model.Order
import com.example.restaurantmanager.data.repository.RestaurantRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    fun placeOrder(
        customerName: String,
        cartItems: List<CartItem>,
        total: Double,
        onOrderPlaced: (Int) -> Unit
    ) {
        viewModelScope.launch {
            val order = Order(
                customer_name = customerName,
                items = Gson().toJson(cartItems),
                total = total,
                status = "Pending",
                created_at = System.currentTimeMillis()
            )
            val orderId = repository.insertOrder(order).toInt()
            onOrderPlaced(orderId)
        }
    }
}
