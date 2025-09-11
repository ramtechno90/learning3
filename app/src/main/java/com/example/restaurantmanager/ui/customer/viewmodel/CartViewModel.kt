package com.example.restaurantmanager.ui.customer.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.restaurantmanager.data.local.model.CartItem
import com.example.restaurantmanager.data.local.model.MenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {
    var cartItems = mutableStateListOf<CartItem>()
        private set

    fun addItem(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.id == menuItem.id }
        if (existingItem != null) {
            existingItem.dineInQuantity++
        } else {
            cartItems.add(CartItem(menuItem, 1, 0, ""))
        }
    }

    fun removeItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }

    fun clear() {
        cartItems.clear()
    }

    fun onDineInQuantityChanged(item: CartItem, quantity: Int) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = item.copy(dineInQuantity = quantity.coerceAtLeast(0))
        }
    }

    fun onTakeawayQuantityChanged(item: CartItem, quantity: Int) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = item.copy(takeawayQuantity = quantity.coerceAtLeast(0))
        }
    }

    fun onInstructionsChanged(item: CartItem, instructions: String) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            cartItems[index] = item.copy(instructions = instructions)
        }
    }

    val subtotal: Double
        get() = cartItems.sumOf { it.menuItem.price * (it.dineInQuantity + it.takeawayQuantity) }

    val parcelCharges: Double
        get() = cartItems.sumOf { it.menuItem.parcel_charge * it.takeawayQuantity }

    val grandTotal: Double
        get() = subtotal + parcelCharges
}
