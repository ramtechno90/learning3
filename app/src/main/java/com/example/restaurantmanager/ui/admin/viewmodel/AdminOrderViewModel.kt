package com.example.restaurantmanager.ui.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanager.data.local.model.Order
import com.example.restaurantmanager.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminOrderViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    val pendingOrders: StateFlow<List<Order>> = repository.getOrdersByStatus("Pending")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val acceptedOrders: StateFlow<List<Order>> = repository.getOrdersByStatus("Accepted")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedOrders: StateFlow<List<Order>> = repository.getOrdersByStatus("Completed")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rejectedOrders: StateFlow<List<Order>> = repository.getOrdersByStatus("Rejected")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateOrderStatus(order: Order, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = newStatus))
        }
    }

    fun clearCompletedOrders() {
        viewModelScope.launch {
            repository.clearOrdersByStatus(listOf("Completed"))
        }
    }

    fun clearRejectedOrders() {
        viewModelScope.launch {
            repository.clearOrdersByStatus(listOf("Rejected"))
        }
    }
}
