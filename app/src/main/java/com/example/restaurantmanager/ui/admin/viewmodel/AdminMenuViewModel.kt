package com.example.restaurantmanager.ui.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanager.data.local.model.Category
import com.example.restaurantmanager.data.local.model.MenuItem
import com.example.restaurantmanager.data.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMenuViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    val menuItems: StateFlow<List<MenuItem>> = repository.getAllMenuItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.insertMenuItem(menuItem)
        }
    }

    fun updateMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.updateMenuItem(menuItem)
        }
    }

    fun deleteMenuItem(menuItem: MenuItem) {
        viewModelScope.launch {
            repository.deleteMenuItem(menuItem)
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }
}
