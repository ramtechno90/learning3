package com.example.restaurantmanager.ui.customer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    val menuItems: StateFlow<Map<String, List<MenuItem>>> = repository.getAllMenuItems()
        .map { items ->
            items.groupBy { it.category }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
}
